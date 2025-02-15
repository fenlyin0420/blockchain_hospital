<template>
  <div class="code-highlighter">
    <pre v-html="highlightedCode"></pre>
  </div>
</template>

<script>
import hljs from 'highlight.js';
import 'highlight.js/styles/default.css';

export default {
  name: 'CodeHighlighter',
  props: {
    code: {
      type: String,
      required: true,
    },
    language: {
      type: String,
      default: 'plaintext',
    },
  },
  data() {
    return {
      highlightedCode: '',
    };
  },
  watch: {
    code: {
      immediate: true,
      handler(newCode) {
        this.formatAndHighlightCode(newCode);
      },
    },
  },
  methods: {
    async formatAndHighlightCode(code) {
      try {
        // 自动格式化代码
        const formattedCode = await this.formatCode(code, this.language);
        this.$message.success('代码格式化成功');
        // 高亮代码
        this.highlightedCode = hljs.highlight(formattedCode, { language: this.language }).value;
      } catch (error) {
        console.error('Error formatting or highlighting code:', error);
      }
    },
    async formatCode(code, language) {
      if (language === 'json') {
        // JSON 格式化
        return JSON.stringify(JSON.parse(code), null, 2);
      } else {
        // 其他语言的格式化逻辑可以在这里添加
        return code;
      }
    },
  },
  mounted() {
    this.formatAndHighlightCode(this.code);
  },
};
</script>

<style scoped>
.code-highlighter {
  background-color: #f8f8f8;
  border-radius: 4px;
  overflow-x: auto;
}

.code-highlighter pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  padding: 1rem;
  font-family: monospace;
  font-size: 14px;
  line-height: 1.5;
  color: #333;
}
</style>