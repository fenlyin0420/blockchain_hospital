// 导入密钥压缩函数
import { compressKey, decompressKey } from './keyCompression';

// 测试密钥
const testKey = 'MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBBIIBVTCCAVECAQEEIJORHTmOPm+yMbmLo8WvPqB4zTI+Ejs46pCLzZvbYt1soIHjMIHgAgEBMCwGByqGSM49AQECIQD////+/////////////////////wAAAAD//////////zBEBCD////+/////////////////////wAAAAD//////////AQgKOn6np2fXjRNWp5Lz2UJp/OXifUVq4+S3by9QU2UDpMEQQQyxK4sHxmBGV+ZBEZqOcmUj+MLv/JmC+FxWkWJM0x0x7w3NqL09necWb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA/////v///////////////3ID32shxgUrU7v0CTnVQSMCAQGhRANCAAQ2V0KanwItMh4a8Lcs9sqDNqPN2JCOjStKgFbPgxjufeTeRw8/CHwHwzhd9KBAIoWCb3iIbuSwKZf/r10nragV';

// 测试函数
export function testKeyCompression() {
  console.log('测试密钥压缩解压功能');
  console.log('-----------------');
  
  // 打印原始密钥信息
  console.log(`原始密钥长度: ${testKey.length} 字符`);
  console.log(`原始密钥示例: ${testKey.substring(0, 40)}...`);
  
  // 压缩密钥
  console.time('压缩耗时');
  const compressed = compressKey(testKey);
  console.timeEnd('压缩耗时');
  
  // 打印压缩结果
  console.log(`压缩后长度: ${compressed.length} 字符`);
  console.log(`压缩后示例: ${compressed.substring(0, 40)}...`);
  console.log(`压缩率: ${Math.round((1 - compressed.length/testKey.length) * 100)}%`);
  
  // 解压缩
  console.time('解压耗时');
  const decompressed = decompressKey(compressed);
  console.timeEnd('解压耗时');
  
  // 验证解压结果
  const isEqual = decompressed === testKey;
  console.log(`解压后长度: ${decompressed.length} 字符`);
  console.log(`解压还原成功: ${isEqual ? '✓' : '✗'}`);
  
  if (!isEqual) {
    console.log('解压后的密钥与原始密钥不匹配。');
    console.log(`原始密钥: ${testKey.substring(0, 20)}...`);
    console.log(`解压结果: ${decompressed.substring(0, 20)}...`);
  }
  
  return {
    originalLength: testKey.length,
    compressedLength: compressed.length,
    compressionRatio: Math.round((1 - compressed.length/testKey.length) * 100),
    restoredSuccessfully: isEqual
  };
}

// 可以在浏览器控制台中调用此函数进行测试
window.testKeyCompression = testKeyCompression; 