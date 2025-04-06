import axios from 'axios';

// 创建API请求实例
const chatAPI = axios.create({
  baseURL: process.env.VUE_APP_AI_ENDPIONT,
  timeout: 60000, // 60s超时，AI生成可能需要较长时间
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + process.env.VUE_APP_APIKEY
  }
});

// 设置请求拦截器
chatAPI.interceptors.request.use(config => {
  return config;
}, error => {
  return Promise.reject(error);
});

// 设置响应拦截器
chatAPI.interceptors.response.use(response => {
  return response.data;
}, error => {
  return Promise.reject(error);
});

/**
 * 医疗助手聊天服务
 */
export const medicalAdvisorAPI = {
  /**
   * 向通义千问发送聊天消息
   * @param {Array} messages - 聊天历史记录，格式为[{role:'user|assistant', content:'消息内容'}]
   * @returns {Promise} 返回AI响应
   */
  chat(messages = []) {
    return chatAPI.post('chat/completions', {
      model: process.env.VUE_APP_MODEL,
      messages,
      stream: false
    });
  },

  /**
   * 使用流式响应发送聊天消息
   * @param {Array} messages - 聊天历史记录，格式为[{role:'user|assistant', content:'消息内容'}]
   * @param {Object} callbacks - 回调函数对象，包含 onMessage, onComplete, onError
   */
  chatStream(messages = [], callbacks = {}) {
    const { onMessage, onComplete, onError } = callbacks;
    
    // 创建 EventSource 连接的URL和请求头
    const controller = new AbortController();
    const { signal } = controller;
    
    // 创建请求参数
    const requestBody = {
      model: process.env.VUE_APP_MODEL,
      messages,
      stream: true
    };
    
    // 使用fetch API进行SSE连接
    fetch(`${process.env.VUE_APP_AI_ENDPIONT}/chat/completions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + process.env.VUE_APP_APIKEY
      },
      body: JSON.stringify(requestBody),
      signal
    })
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      
      // 获取响应数据流
      const reader = response.body.getReader();
      const decoder = new TextDecoder('utf-8');
      let buffer = '';
      
      // 接收数据流的函数
      const processStream = ({ done, value }) => {
        if (done) {
          // 处理缓冲区中剩余的数据
          if (buffer.trim() && onMessage) {
            onMessage(buffer);
          }
          if (onComplete) onComplete();
          return;
        }
        
        // 解码当前数据块并添加到缓冲区
        buffer += decoder.decode(value, { stream: true });
        
        // 处理数据块，可能包含多个SSE消息
        const lines = buffer.split('\n\n');
        
        // 保留最后一个可能不完整的块
        buffer = lines.pop() || '';
        
        for (const line of lines) {
          if (line.trim() === '') continue;
          
          if (line.startsWith('data: ')) {
            const data = line.slice(6).trim(); // 移除 'data: ' 前缀并去除空格
            if (data && onMessage) {
              onMessage(data);
            }
          }
        }
        
        // 继续读取下一个数据块
        reader.read().then(processStream).catch(error => {
          if (onError) onError(error);
        });
      };
      
      // 开始读取数据流
      reader.read().then(processStream).catch(error => {
        if (onError) onError(error);
      });
    })
    .catch(error => {
      if (onError) onError(error);
    });
    
    // 返回用于取消请求的控制器
    return controller;
  }
};

export default chatAPI; 