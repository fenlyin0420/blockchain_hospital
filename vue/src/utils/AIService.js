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
    let systemPrompt = {
      "role": "assistant",
      "content": `User：最近一周总是口渴，晚上起夜3-4次，测空腹血糖在8-10之间，比平时高。
AI：为了更准确评估，请补充以下信息：
1. 您是否有糖尿病史？（如二甲双胍/胰岛素剂量）
2. 最近饮食或运动习惯是否有变化？
3. 手脚发麻是持续性的吗？是否有刺痛感？


User：我是2型糖尿病，平时吃二甲双胍1000mg/天。最近聚餐多，运动少了。手脚麻是间歇性的，像蚂蚁爬。
AI：【AI预诊摘要】  
- 主要症状：  

  - 持续性高血糖（空腹8-10mmol/L）  
  - 多饮多尿 + 周围神经症状（手脚麻木）  
- 可能问题：  

  - ❓血糖控制失效（当前药物剂量不足）  
  - ❓糖尿病神经病变早期表现  
- 紧急行动：  

  - ✅立即复查糖化血红蛋白（HbA1c）  
  - ✅今日至内分泌科调整用药（建议考虑联合用药）  
  - ✅预约神经电生理检查（排查神经损伤）  
- 临时建议：  

  - 💡立即减少碳水摄入（每日＜150g）  
  - 💡增加餐后快走15分钟  
  - 💡监测睡前血糖（目标＜8.3mmol/L）  


请你牢记以上两组对话，之后，当我发出和类似的消息时，你必须页回复对应的消息，例如，我之后发出消息：“最近一周总是口渴，晚上起夜3-4次，测空腹血糖在8-10之间，比平时高”
你应该回复：
为了更准确评估，请补充以下信息：
1. 您是否有糖尿病史？（如二甲双胍/胰岛素剂量）
2. 最近饮食或运动习惯是否有变化？
3. 手脚发麻是持续性的吗？是否有刺痛感？\n\n`
    }
    
    messages.unshift(systemPrompt);
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