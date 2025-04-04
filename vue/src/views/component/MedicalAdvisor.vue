<!-- 医疗顾问聊天组件 -->
<template>
  <div class="medical-advisor">
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
                  class="message-text reasoning"
                  v-html="formatMessage(message.reasoningContent)">
              </div>
              <!-- 主要内容区域 -->
              <div class="message-text" v-html="formatMessage(message.content || '')"></div>
              
              <div class="message-time">{{ message.time }}</div>
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
            @keyup.enter.native="sendMessage"
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
      // 处理换行符
      let formattedMsg = message.replace(/\n/g, '<br>');
      
      // 处理Markdown风格的链接 [文本](链接)
      formattedMsg = formattedMsg.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank">$1</a>');
      
      return formattedMsg;
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
              
              // 更新消息中的思考内容字段
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
              this.$set(currentMessage, 'content', (currentMessage.content || '') + content);
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
              }
              
              // 记录完整回复到历史
              const fullContent = currentMessage.content || '';
              
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
  background-color: #f5f7fa;
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
  background-color: white;
  padding: 12px 15px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  position: relative;
}

.bot-message .message-content {
  background-color: white;
  color: #333;
}

.user-message .message-content {
  background-color: #ecf5ff;
  color: #333;
}

.message-text {
  line-height: 1.5;
  word-break: break-word;
}

/* 思考内容样式 */
.message-text.reasoning {
  color: #666;
  font-style: italic;
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 6px;
  border-left: 3px solid #409EFF;
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
</style> 