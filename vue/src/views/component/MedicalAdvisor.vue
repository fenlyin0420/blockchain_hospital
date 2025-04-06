<!-- 医疗顾问聊天组件 -->
<template>
  <div class="medical-advisor markdown-body">
    <el-drawer
      :visible.sync="drawerVisible"
      direction="rtl"
      size="30%"
      :with-header="false"
      :before-close="handleClose"
    >
      <div class="advisor-container">
        <!-- 标题区域 -->
        <div class="advisor-header">
          <div class="title">
            <img src="@/assets/imgs/AI.png" alt="AI医疗顾问" class="advisor-avatar">
            <h3>AI医疗助手</h3>
          </div>
          <div class="header-actions">
            <el-switch
              v-model="showReasoning"
              active-text="显示思考过程"
              inactive-text=""
              class="reasoning-switch"
            ></el-switch>
            <el-button 
              type="text" 
              icon="el-icon-close" 
              @click="drawerVisible = false" 
              class="close-btn"
            ></el-button>
          </div>
        </div>

        <!-- 聊天内容区域 -->
        <div class="chat-container" ref="chatContainer">
          <div 
            v-for="(message, index) in chatMessages" 
            :key="index" 
            class="message" 
            :class="{'bot-message': message.sender === 'bot', 'user-message': message.sender === 'user'}"
          >
            <div v-if="message.sender === 'bot'" class="avatar">
              <img src="@/assets/imgs/AI.png" alt="AI">
            </div>
            <div v-else class="avatar">
              <img src="@/assets/imgs/doctor.png" alt="医生">
            </div>
            <div class="message-content">
              <!-- 思考内容区域 - 确保在有reasoningContent且不等于content时显示 -->
              <div v-if="message.sender === 'bot' && message.reasoningContent && message.reasoningContent !== message.content && showReasoning" 
                  class="message-text reasoning markdown-body"
                  v-html="formatMessage(message.reasoningContent)">
              </div>
              <!-- 主要内容区域 -->
              <div class="message-text markdown-body" v-html="formatMessage(message.content || '')"></div>
            </div>
          </div>
          
          <!-- 加载中显示 -->
          <div v-if="loading" class="message bot-message">
            <div class="avatar">
              <img src="@/assets/imgs/AI.png" alt="AI">
            </div>
            <div class="message-content">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="input-container">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            v-model="inputMessage"
            placeholder="请输入您的医疗问题..."
            @keydown.enter.native="handleEnterKey"
          ></el-input>
          <div class="input-actions">
            <el-button 
              type="info" 
              icon="el-icon-delete" 
              @click="clearChat"
            >清空对话</el-button>
            <el-button 
              type="primary" 
              icon="el-icon-s-promotion" 
              :disabled="!inputMessage.trim() || loading"
              @click="sendMessage"
            >发送</el-button>

          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { medicalAdvisorAPI } from '@/utils/AIService';
import MarkdownIt from 'markdown-it';
import DOMPurify from 'dompurify';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';

// 配置 markdown-it
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
  highlight: function(str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return '<pre class="hljs"><code>' +
               hljs.highlight(str, { language: lang, ignoreIllegals: true }).value +
               '</code></pre>';
      } catch (__) {}
    }
    // 对未指定语言的代码块使用普通高亮
    return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>';
  }
});

// 调整 markdown-it 配置以获得更好的输出
md.renderer.rules.table_open = () => '<div class="table-container"><table class="md-table">';
md.renderer.rules.table_close = () => '</table></div>';

export default {
  name: "MedicalAdvisor",
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      drawerVisible: false,
      inputMessage: "",
      chatMessages: [],
      loading: false,
      messages: [], // 用于API请求的聊天历史
      currentReasoningContent: "", // 存储当前思考内容
      showReasoning: true // 控制是否显示思考过程
    };
  },
  watch: {
    visible(newVal) {
      this.drawerVisible = newVal;
      if (newVal && this.chatMessages.length === 0) {
        // 第一次打开时发送欢迎消息
        this.addBotMessage("您好，我是AI医疗助手，可以帮你规范书写病历，查询药物用法用量。");
      }
    },
    drawerVisible(newVal) {
      if (!newVal) {
        this.$emit('update:visible', false);
      }
    },
    chatMessages() {
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    }
  },
  methods: {
    handleClose(done) {
      this.$emit('update:visible', false);
      done();
    },
    formatMessage(message) {
      if (!message) return '';
      
      try {
        // 使用 markdown-it 解析 markdown
        const renderedHtml = md.render(message);
        
        // 使用 DOMPurify 净化 HTML 防止 XSS 攻击
        const sanitizedHtml = DOMPurify.sanitize(renderedHtml, {
          ADD_ATTR: ['target'], // 允许链接使用 target="_blank"
          FORBID_TAGS: ['style', 'script', 'iframe'], // 禁止可能导致问题的标签
          FORBID_ATTR: ['style', 'onerror', 'onload'], // 禁止可能导致问题的属性
        });
        
        return sanitizedHtml;
      } catch (error) {
        console.error('Markdown parsing error:', error);
        // 出错时返回转义后的原始内容
        return message
          .replace(/</g, '&lt;')
          .replace(/>/g, '&gt;')
          .replace(/\n/g, '<br>');
      }
    },
    sendMessage() {
      const message = this.inputMessage.trim();
      if (!message || this.loading) return;
      
      // 添加用户消息到聊天记录
      this.addUserMessage(message);
      this.inputMessage = "";
      
      // 更新聊天历史
      this.messages.push({
        role: 'user',
        content: message
      });
      
      this.loading = true;
      
      // 重置思考内容
      this.currentReasoningContent = "";
      
      // 添加一个空的机器人消息作为流式响应的容器
      const botMessageIndex = this.chatMessages.length;
      this.addBotMessage("");
      
      // 流式响应处理
      medicalAdvisorAPI.chatStream(this.messages, {
        onMessage: (chunk) => {
          try {
            if (chunk === "[DONE]") return;
            
            const data = JSON.parse(chunk);
            if (!data.choices || data.choices.length === 0) return;
            
            const choice = data.choices[0];
            const delta = choice.delta || {};
            
            // 当前消息对象
            const currentMessage = this.chatMessages[botMessageIndex];
            
            // 检查delta中是否包含reasoning_content字段（无论值是什么）
            if ('reasoning_content' in delta) {
              const reasoningContent = delta.reasoning_content || '';
              
              // 累积思考内容
              this.currentReasoningContent += reasoningContent;
              
              // 更新消息中的思考内容字段，但不立即渲染
              this.$set(currentMessage, 'reasoningContent', this.currentReasoningContent);
              
              // 如果尚未开始接收正式内容，在内容区域也显示思考内容
              if (!currentMessage.hasContent) {
                this.$set(currentMessage, 'content', this.currentReasoningContent);
              }
            }
            
            // 检查delta中是否包含content字段（无论值是什么）
            if ('content' in delta) {
              const content = delta.content || '';
              
              // 如果这是第一个content块
              if (!currentMessage.hasContent) {
                // 将思考内容保存下来，并清空内容准备接收正式内容
                this.$set(currentMessage, 'hasContent', true);
                
                // 如果之前显示的是思考内容，现在清空
                if (currentMessage.content === this.currentReasoningContent) {
                  this.$set(currentMessage, 'content', '');
                }
              }
              
              // 追加内容
              const updatedContent = (currentMessage.content || '') + content;
              this.$set(currentMessage, 'content', updatedContent);
              currentMessage.rawContent = updatedContent;
            }
            
            // 如果收到完成信号
            if (choice.finish_reason === "stop") {
              console.log("收到停止信号，当前状态:", {
                hasContent: currentMessage.hasContent,
                content: currentMessage.content,
                reasoningContent: currentMessage.reasoningContent,
                currentReasoning: this.currentReasoningContent
              });
              
              // 如果有思考内容但没有正式内容
              if (this.currentReasoningContent && (!currentMessage.content || currentMessage.content === '')) {
                this.$set(currentMessage, 'content', this.currentReasoningContent);
                currentMessage.rawContent = this.currentReasoningContent;
              }
              
              // 记录完整回复到历史
              const fullContent = currentMessage.rawContent || '';
              
              this.messages.push({
                role: 'assistant',
                content: fullContent
              });
              
              // 限制历史长度，防止请求过大
              if (this.messages.length > 20) {
                // 保留最近的对话，但始终保留第一条系统引导消息
                const firstMessage = this.messages[0];
                this.messages = [firstMessage, ...this.messages.slice(-19)];
              }
            }
            
            // 滚动到底部
            this.scrollToBottom();
          } catch (e) {
            console.error("解析流式响应失败:", e, chunk);
          }
        },
        onComplete: () => {
          this.loading = false;
        },
        onError: (error) => {
          console.error("流式请求出错:", error);
          const errorMessage = "\n\n(请求出错，请稍后再试)";
          const currentMessage = this.chatMessages[botMessageIndex];
          this.$set(currentMessage, 'content', (currentMessage.content || '') + errorMessage);
          this.loading = false;
          this.currentReasoningContent = "";
        }
      });
    },
    
    addUserMessage(content) {
      this.chatMessages.push({
        sender: "user",
        content: content,
        time: this.getCurrentTime()
      });
    },
    
    addBotMessage(content) {
      this.chatMessages.push({
        sender: "bot",
        content: content,
        reasoningContent: "",
        hasContent: false,
        time: this.getCurrentTime()
      });
    },
    getCurrentTime() {
      const now = new Date();
      const hours = now.getHours().toString().padStart(2, '0');
      const minutes = now.getMinutes().toString().padStart(2, '0');
      return `${hours}:${minutes}`;
    },
    scrollToBottom() {
      const container = this.$refs.chatContainer;
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    },
    clearChat() {
      this.$confirm('确定要清空所有对话记录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.chatMessages = [];
        this.messages = [];
        this.currentReasoningContent = "";
        
        // 重新添加欢迎消息
        this.addBotMessage("您好，我是AI医疗助手，可以帮你规范书写病历，查询药物用法用量。");
        
        // 添加到历史
        this.messages.push({
          role: 'assistant',
          content: "您好，我是AI医疗助手，可以帮你规范书写病历，查询药物用法用量。"
        });
      }).catch(() => {});
    },
    handleEnterKey(event) {
      if (event.shiftKey) {
        return;
      }

      event.preventDefault();
      this.sendMessage();
    }
  }
};
</script>

<style scoped>
.medical-advisor {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
}

.advisor-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #f5f7fa;
}

.advisor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #409EFF;
  color: white;
  border-bottom: 1px solid #dcdfe6;
}

.advisor-header .title {
  display: flex;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.reasoning-switch {
  margin-right: 15px;
}

.reasoning-switch :deep(.el-switch__label) {
  color: white;
}

.reasoning-switch :deep(.el-switch__core) {
  border-color: #ffffff;
}

.reasoning-switch :deep(.el-switch.is-checked .el-switch__core) {
  border-color: #ffffff;
  background-color: #67C23A;
}

.advisor-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
  background-color: white;
}

.close-btn {
  color: white;
  font-size: 20px;
}

.chat-container {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #ffffff;
}

.message {
  display: flex;
  margin-bottom: 20px;
  max-width: 85%;
}

.bot-message {
  margin-right: auto;
}

.user-message {
  margin-left: auto;
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 10px;
  flex-shrink: 0;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-content {
  padding: 12px 15px;
  position: relative;
}

.bot-message .message-content {
  width: 100%;
  color: #333;
}

.user-message .message-content {
  background-color: #ecf5ff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  color: #333;
}

.message-text {
  line-height: 1.5;
  word-break: break-word;
  overflow-wrap: break-word;
}

.message-text p {
  margin: 0 0 0.8em;
}

.message-text p:last-child {
  margin-bottom: 0;
}

.message-text h1, 
.message-text h2, 
.message-text h3, 
.message-text h4, 
.message-text h5, 
.message-text h6 {
  margin: 1em 0 0.5em;
  line-height: 1.25;
  font-weight: 600;
  color: #24292e;
}

.message-text h1:first-child, 
.message-text h2:first-child, 
.message-text h3:first-child {
  margin-top: 0;
}

.message-text h1 {
  font-size: 1.5em;
  padding-bottom: 0.2em;
  border-bottom: 1px solid #eaecef;
}

.message-text h2 {
  font-size: 1.3em;
  padding-bottom: 0.2em;
  border-bottom: 1px solid #eaecef;
}

.message-text h3 {
  font-size: 1.1em;
}

.message-text code {
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  background-color: rgba(27,31,35,0.05);
  border-radius: 3px;
  padding: 0.2em 0.4em;
  font-size: 85%;
}

.message-text pre {
  margin: 0.8em 0;
  padding: 0;
  background-color: transparent;
  overflow: visible;
}

.message-text pre.hljs {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 1em;
  overflow: auto;
  font-size: 85%;
  line-height: 1.45;
}

.message-text pre.hljs code {
  background-color: transparent;
  padding: 0;
  margin: 0;
  border-radius: 0;
  white-space: pre;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
}

.message-text ul,
.message-text ol {
  padding-left: 1.5em;
  margin: 0.5em 0;
}

.message-text li {
  margin: 0.2em 0;
}

.message-text li+li {
  margin-top: 0.1em;
}

.message-text li > p {
  margin: 0;
}

.message-text blockquote {
  margin: 0.8em 0;
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
}

.message-text blockquote > :first-child {
  margin-top: 0;
}

.message-text blockquote > :last-child {
  margin-bottom: 0;
}

.message-text .table-container {
  overflow-x: auto;
  margin: 0.8em 0;
}

.message-text .md-table {
  border-collapse: collapse;
  width: 100%;
  margin: 0;
  display: table;
}

.message-text .md-table th,
.message-text .md-table td {
  border: 1px solid #dfe2e5;
  padding: 0.4em 0.8em;
  text-align: left;
}

.message-text .md-table th {
  font-weight: 600;
  background-color: #f6f8fa;
}

.message-text .md-table tr:nth-child(2n) {
  background-color: #f6f8fa;
}

.message-text a {
  color: #0366d6;
  text-decoration: none;
}

.message-text a:hover {
  text-decoration: underline;
}

.message-text hr {
  height: 0.25em;
  padding: 0;
  margin: 1em 0;
  background-color: #e1e4e8;
  border: 0;
}

.message-text strong {
  font-weight: 600;
}

.message-text img {
  max-width: 100%;
  box-sizing: content-box;
  background-color: #fff;
  margin: 0.5em 0;
}

/* 思考内容样式 */
.message-text.reasoning {
  color: #666;
  font-style: italic;
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 6px;
  border-left: 3px solid #bebfc0;
  margin-bottom: 10px;
  position: relative;
  font-size: 0.95em;
}

.message-text.reasoning::before {
  content: "思考过程";
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
  color: #409EFF;
  font-size: 0.9em;
}

.message-text.reasoning::after {
  content: "";
  position: absolute;
  bottom: -5px;
  left: 20px;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #f5f5f5;
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  text-align: right;
}

.input-container {
  padding: 15px;
  background-color: white;
  border-top: 1px solid #dcdfe6;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.input-actions .el-button {
  margin-left: 10px;
}

/* 打字指示器样式 */
.typing-indicator {
  display: flex;
  align-items: center;
  padding: 5px 10px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background-color: #b6b6b6;
  border-radius: 50%;
  display: inline-block;
  margin: 0 2px;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) {
  animation-delay: 0s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-5px);
  }
}

/* 代码块相关样式 */
.code-block {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 16px;
  overflow: auto;
  margin: 12px 0;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 85%;
}

.code-block code {
  background-color: transparent !important;
  padding: 0 !important;
  border-radius: 0 !important;
  font-family: inherit !important;
}

/* 确保正确展示 */
.markdown-body pre {
  margin: 12px 0;
  padding: 16px;
  overflow: auto;
  background-color: #f6f8fa;
  border-radius: 6px;
  line-height: 1.45;
}

.markdown-body code {
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  padding: 0.2em 0.4em;
  margin: 0;
  font-size: 85%;
  background-color: rgba(27,31,35,0.05);
  border-radius: 3px;
}

.markdown-body pre > code {
  padding: 0;
  margin: 0;
  font-size: 100%;
  word-break: normal;
  white-space: pre;
  background: transparent;
  border: 0;
}

/* 修改代码块样式 */
.markdown-code-block {
  margin: 16px 0;
  background-color: #f6f8fa;
  border-radius: 6px;
  overflow: hidden;
}

.markdown-code-block pre {
  margin: 0 !important;
  padding: 16px;
  overflow: auto;
  background-color: #f6f8fa;
  border-radius: 0 !important;
}

.markdown-code-block code {
  background-color: transparent !important;
  padding: 0 !important;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 85%;
  white-space: pre;
  display: block;
}

/* 代码块样式 */
.code-block-wrapper {
  margin: 16px 0;
  border-radius: 6px;
  overflow: hidden;
  background-color: #f6f8fa;
}

.code-block-wrapper pre {
  margin: 0 !important;
  padding: 16px;
  overflow: auto;
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  font-size: 85%;
  line-height: 1.45;
}

.code-block-wrapper code {
  background-color: transparent !important;
  padding: 0 !important;
  white-space: pre;
}

/* Markdown样式 */
.message-text h1 {
  font-size: 1.5em;
  margin: 16px 0 10px;
  padding-bottom: 0.3em;
  border-bottom: 1px solid #eaecef;
}

.message-text h2 {
  font-size: 1.3em;
  margin: 16px 0 8px;
  padding-bottom: 0.3em;
  border-bottom: 1px solid #eaecef;
}

.message-text h3 {
  font-size: 1.1em;
  margin: 16px 0 8px;
}

.message-text code {
  font-family: SFMono-Regular, Consolas, Liberation Mono, Menlo, monospace;
  background-color: rgba(27,31,35,0.05);
  border-radius: 3px;
  padding: 0.2em 0.4em;
  font-size: 85%;
}

.message-text blockquote {
  margin: 16px 0;
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
}

.message-text ul, .message-text ol {
  padding-left: 2em;
  margin: 16px 0;
}

.message-text li {
  margin: 4px 0;
}

.message-text table {
  border-collapse: collapse;
  margin: 16px 0;
  width: 100%;
}

.message-text table td {
  border: 1px solid #dfe2e5;
  padding: 6px 13px;
}

.message-text strong {
  font-weight: 600;
}

.message-text em {
  font-style: italic;
}

.message-text a {
  color: #0366d6;
  text-decoration: none;
}

.message-text a:hover {
  text-decoration: underline;
}

.message-text hr {
  height: 0.25em;
  padding: 0;
  margin: 24px 0;
  background-color: #e1e4e8;
  border: 0;
}

.message-text p {
  margin: 0 0 12px;
  line-height: 1.5;
}

.message-text p:last-child {
  margin-bottom: 0;
}

.message-text ul, .message-text ol {
  padding-left: 2em;
  margin: 12px 0;
}

.message-text li + li {
  margin-top: 2px;
}

.message-text li > p {
  margin: 0;
}

/* 减少标题上下间距 */
.message-text h1, .message-text h2, .message-text h3 {
  margin-top: 12px;
  margin-bottom: 8px;
}

.message-text h1:first-child, 
.message-text h2:first-child,
.message-text h3:first-child {
  margin-top: 0;
}

/* 优化引用块间距 */
.message-text blockquote {
  margin: 12px 0;
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
}

.message-text blockquote > p {
  margin: 6px 0;
}
</style> 