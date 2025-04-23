<!-- 人脸识别组件 -->
<template>
  <div class="face-verification-container">
    <div v-if="!isVerified" class="face-capture">
      <div class="video-container">
        <video 
          ref="video" 
          id="video" 
          width="100%" 
          height="auto" 
          autoplay 
          playsinline 
          muted
          :class="{ 'face-detecting': isFaceDetecting }"
        ></video>
        <canvas ref="canvas" id="canvas" width="640" height="480" style="display: none;"></canvas>
        
        <!-- 人脸检测指示 -->
        <div v-if="isFaceDetecting" class="face-detecting-indicator">
          <div class="scanning-line"></div>
          <div class="face-frame"></div>
        </div>
        
        <!-- 检测状态提示 -->
        <div class="status-text" :class="{ 'status-error': hasError }">
          {{ statusText }}
        </div>
      </div>
      
      <div class="action-buttons">
        <el-button 
          type="primary" 
          @click="startCamera" 
          :disabled="cameraActive"
        >
          开启摄像头
        </el-button>
        <el-button 
          type="success" 
          @click="captureFace" 
          :disabled="!cameraActive || isFaceDetecting"
        >
          开始识别
        </el-button>
      </div>
    </div>
    
    <!-- 验证成功界面 -->
    <div v-else class="verification-success">
      <div class="success-icon">
        <i class="el-icon-check"></i>
      </div>
      <div class="success-info">
        <h3>人脸验证成功</h3>
        <div v-if="userInfo" class="user-info">
          <div class="user-photo">
            <img src="http://localhost:8090/files/me.jpg" alt="用户照片">
          </div>
          <div class="user-details">
            <p><span>姓名:</span> {{ userInfo.name }}</p>
            <p><span>性别:</span> {{ userInfo.gender }}</p>
            <p><span>年龄:</span> {{ userInfo.age }}岁</p>
            <p><span>相似度:</span> {{ userInfo.similarValue.toFixed(2) }}%</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "FaceVerification",
  props: {
    groupId: {
      type: Number,
      default: 101 // 默认用户组ID
    },
    active: {
      type: Boolean,
      default: true
    },
    autoStart: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      cameraActive: false,
      stream: null,
      canvas: null,
      video: null,
      context: null,
      isFaceDetecting: false,
      statusText: "请开启摄像头并进行面部识别",
      hasError: false,
      isVerified: false,
      userInfo: null,
      captureInterval: null
    };
  },
  watch: {
    active(newVal) {
      if (newVal && this.autoStart) {
        this.startCamera();
      } else if (!newVal && this.cameraActive) {
        this.stopCamera();
      }
    }
  },
  mounted() {
    this.canvas = this.$refs.canvas;
    this.video = this.$refs.video;
    this.context = this.canvas.getContext('2d');
    
    if (this.active && this.autoStart) {
      this.startCamera();
    }
  },
  beforeDestroy() {
    this.stopCamera();
  },
  methods: {
    // 开启摄像头
    startCamera() {
      if (this.cameraActive) return;
      
      this.hasError = false;
      this.statusText = "正在开启摄像头...";
      
      // 请求摄像头权限
      navigator.mediaDevices.getUserMedia({
        video: {
          width: { ideal: 640 },
          height: { ideal: 480 },
          facingMode: "user"
        },
        audio: false
      })
      .then(stream => {
        this.stream = stream;
        this.video.srcObject = stream;
        this.cameraActive = true;
        this.statusText = "摄像头已开启，请将面部对准摄像头";
      })
      .catch(err => {
        this.hasError = true;
        this.statusText = "摄像头开启失败: " + err.message;
        console.error("摄像头开启失败:", err);
      });
    },
    
    // 停止摄像头
    stopCamera() {
      if (this.captureInterval) {
        clearInterval(this.captureInterval);
        this.captureInterval = null;
      }
      
      if (this.stream) {
        this.stream.getTracks().forEach(track => track.stop());
        this.stream = null;
      }
      
      this.cameraActive = false;
      this.isFaceDetecting = false;
    },
    
    // 捕获人脸图像并进行识别
    captureFace() {
      if (!this.cameraActive) return;
      
      this.isFaceDetecting = true;
      this.statusText = "正在检测人脸...";
      
      // 绘制当前视频帧到Canvas
      this.context.drawImage(this.video, 0, 0, this.canvas.width, this.canvas.height);
      
      // 将Canvas转换为base64图像数据
      const imageData = this.canvas.toDataURL('image/png');
      
      this.searchFace(imageData);

      // 首先检测是否有人脸
      // this.detectFaces(base64Data).then(faceCount => {
      //   if (faceCount > 0) {
      //     this.statusText = "检测到人脸，正在进行识别...";
      //   } else {
      //     this.isFaceDetecting = false;
      //     this.hasError = true;
      //     this.statusText = "未检测到人脸，请调整姿势后重试";
      //   }
      // }).catch(err => {
      //   this.isFaceDetecting = false;
      //   this.hasError = true;
      //   this.statusText = "人脸检测失败: " + err.message;
      // });
    },
    
    // 检测人脸
    detectFaces(imageBase64) {
      return new Promise((resolve, reject) => {
        this.$faceRequest.post('/detectFaces', { image: imageBase64 })
          .then(res => {
            if (res.code === 200 && Array.isArray(res.data)) {
              resolve(res.data.length);
            } else {
              resolve(0);
            }
          })
          .catch(err => {
            reject(err);
          });
      });
    },
    
    // 搜索人脸
    searchFace(imageBase64) {
      const formData = new FormData()
      formData.append('groupId', this.groupId)
      formData.append('file', imageBase64);
      this.$faceRequest.post('/faceSearch', formData)
      .then(res => {
        this.isFaceDetecting = false;
        console.log('res', res)
        if (res.success) {
          // 人脸识别成功
          this.isVerified = true;
          this.userInfo = res.data;
          
          // 发送成功事件
          this.$emit('verification-success', res.data);
        } else {
          // 人脸识别失败
          this.hasError = true;
          this.statusText = res.msg || "人脸不匹配，请重试";
        }
      })
      .catch(err => {
        this.isFaceDetecting = false;
        this.hasError = true;
        this.statusText = "人脸识别失败: " + (err.message || "网络错误");
        console.error("人脸识别错误:", err);
      });
    },
    
    // 重置验证状态
    reset() {
      this.isVerified = false;
      this.userInfo = null;
      this.hasError = false;
      this.statusText = "请开启摄像头并进行面部识别";
      this.isFaceDetecting = false;
      
      if (this.autoStart) {
        this.startCamera();
      }
    }
  }
};
</script>

<style scoped>
.face-verification-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  padding: 10px;
}

.face-capture {
  width: 100%;
  max-width: 500px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.video-container {
  position: relative;
  width: 100%;
  padding-top: 75%; /* 4:3 Aspect Ratio */
  background-color: #222;
  overflow: hidden;
}

.video-container video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transform: scaleX(-1); /* Mirror video */
}

.face-detecting-indicator {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.scanning-line {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, transparent, #409eff, transparent);
  animation: scan 2s ease-in-out infinite;
}

.face-frame {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 200px;
  height: 200px;
  transform: translate(-50%, -50%);
  border: 2px solid #409eff;
  border-radius: 50%;
  box-shadow: 0 0 10px rgba(64, 158, 255, 0.5);
}

.status-text {
  position: absolute;
  bottom: 10px;
  left: 0;
  width: 100%;
  padding: 5px;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  text-align: center;
  font-size: 14px;
}

.status-error {
  background-color: rgba(245, 108, 108, 0.7);
}

.action-buttons {
  display: flex;
  justify-content: space-around;
  padding: 15px;
  background-color: #f5f7fa;
}

/* 验证成功样式 */
.verification-success {
  width: 100%;
  max-width: 500px;
  padding: 20px;
  border-radius: 8px;
  background-color: #f0f9eb;
  text-align: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.success-icon {
  font-size: 60px;
  color: #67c23a;
  margin-bottom: 20px;
}

.success-info h3 {
  color: #67c23a;
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
}

.user-photo {
  width: 120px;
  height: 120px;
  overflow: hidden;
  border-radius: 8px;
  margin-right: 20px;
  border: 2px solid #dcdfe6;
}

.user-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-details {
  text-align: left;
}

.user-details p {
  margin: 10px 0;
  color: #606266;
}

.user-details p span {
  font-weight: bold;
  display: inline-block;
  width: 70px;
}

.face-detecting video {
  border: 2px solid #409eff;
}

@keyframes scan {
  0% {
    top: 0;
  }
  50% {
    top: 100%;
  }
  100% {
    top: 0;
  }
}

@media (max-width: 768px) {
  .user-info {
    flex-direction: column;
  }
  
  .user-photo {
    margin-right: 0;
    margin-bottom: 15px;
  }
}
</style> 