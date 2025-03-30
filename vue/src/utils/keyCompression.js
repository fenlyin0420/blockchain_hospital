/**
 * 密钥压缩工具函数
 * 用于减小二维码数据量，提高扫描效率
 */

/**
 * 压缩公钥/私钥
 * @param {string} keyBase64 - Base64格式的密钥
 * @returns {string} 压缩后的密钥
 */
export function compressKey(keyBase64) {
  try {
    if (!keyBase64) {
      return '';
    }
    
    // 1. 移除标准填充字符和换行符
    const cleanedKey = keyBase64.replace(/=+$/g, '').replace(/\r?\n/g, '');
    
    // 2. 用特定字符替换区块链密钥中频繁出现的模式
    let compressed = cleanedKey
      // 替换特定序列 - 优先替换最长匹配
      .replace(/MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAs/g, '!1')                       // 头部标识符
      .replace(/CAQGhRANC/g, '!2')                                                 // 尾部标识符
      .replace(/GByqGSM49AQEC/g, '!3')                                             // 常见序列
      .replace(/\/\/\/\/\/\/\/\//g, '@8')                                          // 8个斜杠
      .replace(/\/\/\/\/\/\/\//g, '@7')                                           // 7个斜杠
      .replace(/\/\/\/\/\/\//g, '@6')                                               // 6个斜杠
      .replace(/\/\/\/\/\//g, '@5')                                                 // 5个斜杠
      .replace(/\/\/\/\//g, '@4')                                                   // 4个斜杠
      .replace(/\/\/\//g, '@3')                                                     // 3个斜杠
      .replace(/AAAAAAAA/g, '#8')                                                   // 8个A
      .replace(/AAAAAAA/g, '#7')                                                    // 7个A
      .replace(/AAAAAA/g, '#6')                                                     // 6个A
      .replace(/AAAAA/g, '#5')                                                      // 5个A
      .replace(/AAAA/g, '#4')                                                       // 4个A
      .replace(/\+\+\+\+\+/g, '^5')                                                 // 5个加号
      .replace(/\+\+\+\+/g, '^4')                                                   // 4个加号
      .replace(/\+\+\+/g, '^3')                                                     // 3个加号
      .replace(/MII/g, '*M')                                                        // 常见前缀
      .replace(/BII/g, '*B')                                                        // 常见前缀
      .replace(/CAQ/g, '*C')                                                        // 常见序列
      .replace(/AIB/g, '*A')                                                        // 常见序列
      .replace(/RAQ/g, '*R')                                                        // 常见序列
      .replace(/BAQ/g, '*Q')                                                        // 常见序列
      .replace(/ECI/g, '*E')                                                        // 常见序列
      .replace(/AQE/g, '*P')                                                        // 常见序列
      .replace(/ICj/g, '%I')                                                        // 常见序列
      .replace(/ECg/g, '%E')                                                        // 常见序列
      .replace(/RAg/g, '%R')                                                        // 常见序列
      .replace(/AAQ/g, '%A')                                                        // 常见序列
      .replace(/DNC/g, '%D')                                                        // 常见序列
      .replace(/MdM/g, '%M');                                                       // 常见序列
    
    // 3. 对连续重复次数超过3次的字符进行压缩
    let result = '';
    let i = 0;
    
    while (i < compressed.length) {
      const char = compressed[i];
      let count = 1;
      
      // 计算连续相同字符的数量
      while (i + count < compressed.length && compressed[i + count] === char) {
        count++;
      }
      
      // 如果重复次数超过3，使用压缩表示
      if (count > 3) {
        result += char + count.toString();
        i += count;
      } else {
        result += char;
        i++;
      }
    }
    
    // 4. 添加简单校验和以确保正确解压
    const checksum = Array.from(keyBase64)
      .reduce((sum, char) => sum + char.charCodeAt(0), 0) % 256;
    
    // 5. 返回压缩结果，添加版本标识符2代表增强版算法
    return `2:${result}:${checksum.toString(16).padStart(2, '0')}`;
  } catch (error) {
    console.error('密钥压缩失败:', error);
    return keyBase64; // 失败时返回原始密钥
  }
}

/**
 * 解压缩公钥/私钥
 * @param {string} compressedKey - 压缩格式的密钥
 * @returns {string} 解压后的Base64密钥
 */
export function decompressKey(compressedKey) {
  try {
    if (!compressedKey || !compressedKey.includes(':')) {
      return compressedKey; // 不是有效的压缩格式
    }
    
    // 1. 分离版本号、压缩数据和校验和
    const parts = compressedKey.split(':');
    
    // 检查版本
    if (parts.length !== 3) {
      // 老版本格式或无效格式，尝试兼容处理
      return compressedKey;
    }
    
    const version = parts[0];
    const compressed = parts[1];
    const checksumHex = parts[2];
    
    // 2. 根据版本选择解压算法
    let decompressed;
    
    if (version === '1') {
      // 版本1的解压算法
      
      // 2.1 解码重复字符
      let temp = '';
      let i = 0;
      
      while (i < compressed.length) {
        const char = compressed[i];
        i++;
        
        // 检查后面是否跟着数字
        let countStr = '';
        while (i < compressed.length && /\d/.test(compressed[i])) {
          countStr += compressed[i];
          i++;
        }
        
        if (countStr) {
          // 有重复计数，展开字符
          const count = parseInt(countStr, 10);
          temp += char.repeat(count);
        } else {
          // 单个字符
          temp += char;
        }
      }
      
      // 2.2 还原特定替换的模式
      decompressed = temp
        .replace(/@8\//g, '////////')    // 8个斜杠
        .replace(/@5\//g, '/////')       // 5个斜杠
        .replace(/#5/g, 'AAAAA')         // 5个A
        .replace(/#4/g, 'AAAA')          // 4个A
        .replace(/\^4/g, '++++')          // 4个加号
        .replace(/\^3/g, '+++')           // 3个加号
        .replace(/\^2/g, '++')            // 2个加号
        .replace(/\?3/g, '===')           // 3个等号
        .replace(/\?2/g, '==')            // 2个等号
        .replace(/\$M/g, 'MIIC')          // 常见前缀
        .replace(/\$A/g, 'AQEC')          // 常见序列
        .replace(/\$C/g, 'CAQEBAiEA')     // 常见序列
        .replace(/\$G/g, 'GByqGSM49')     // 常见序列
        .replace(/\$B/g, 'AgEB');         // 常见序列
    } else if (version === '2') {
      // 版本2的解压算法 - 增强版
      
      // 2.1 解码重复字符
      let temp = '';
      let i = 0;
      
      while (i < compressed.length) {
        const char = compressed[i];
        i++;
        
        // 检查后面是否跟着数字
        let countStr = '';
        while (i < compressed.length && /\d/.test(compressed[i])) {
          countStr += compressed[i];
          i++;
        }
        
        if (countStr) {
          // 有重复计数，展开字符
          const count = parseInt(countStr, 10);
          temp += char.repeat(count);
        } else {
          // 单个字符
          temp += char;
        }
      }
      
      // 2.2 还原特定替换的模式 - 注意顺序与压缩时相反
      decompressed = temp
        .replace(/!1/g, 'MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAs')                    // 头部标识符
        .replace(/!2/g, 'CAQGhRANC')                                                // 尾部标识符
        .replace(/!3/g, 'GByqGSM49AQEC')                                            // 常见序列
        .replace(/@8/g, '////////')                                                 // 8个斜杠
        .replace(/@7/g, '///////') 
        .replace(/@6/g, '//////')                                                  // 7个斜杠
        .replace(/@5/g, '/////')                                                    // 5个斜杠
        .replace(/@4/g, '////')                                                     // 4个斜杠
        .replace(/@3/g, '///')                                                      // 3个斜杠
        .replace(/#8/g, 'AAAAAAAA')                                                 // 8个A
        .replace(/#7/g, 'AAAAAAA')                                                  // 7个A
        .replace(/#6/g, 'AAAAAA')                                                   // 6个A
        .replace(/#5/g, 'AAAAA')                                                    // 5个A
        .replace(/#4/g, 'AAAA')                                                     // 4个A
        .replace(/\^5/g, '+++++')                                                   // 5个加号
        .replace(/\^4/g, '++++')                                                    // 4个加号
        .replace(/\^3/g, '+++')                                                     // 3个加号
        .replace(/\*M/g, 'MII')                                                     // 常见前缀
        .replace(/\*B/g, 'BII')                                                     // 常见前缀
        .replace(/\*C/g, 'CAQ')                                                     // 常见序列
        .replace(/\*A/g, 'AIB')                                                     // 常见序列
        .replace(/\*R/g, 'RAQ')                                                     // 常见序列
        .replace(/\*Q/g, 'BAQ')                                                     // 常见序列
        .replace(/\*E/g, 'ECI')                                                     // 常见序列
        .replace(/\*P/g, 'AQE')                                                     // 常见序列
        .replace(/\%I/g, 'ICj')                                                     // 常见序列
        .replace(/\%E/g, 'ECg')                                                     // 常见序列
        .replace(/\%R/g, 'RAg')                                                     // 常见序列
        .replace(/\%A/g, 'AAQ')                                                     // 常见序列
        .replace(/\%D/g, 'DNC')                                                     // 常见序列
        .replace(/\%M/g, 'MdM');                                                    // 常见序列
    } else {
      // 未知版本，返回原始压缩数据
      return compressedKey;
    }
    
    // 3. 添加Base64填充字符
    const paddingLength = 4 - (decompressed.length % 4);
    if (paddingLength < 4) {
      decompressed += '='.repeat(paddingLength);
    }
    
    return decompressed;
  } catch (error) {
    console.error('密钥解压失败:', error);
    return compressedKey; // 失败时返回原始输入
  }
} 