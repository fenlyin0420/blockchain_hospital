<template>
  <div class="main-content">
    <h1 style="text-align: center;">私钥获取</h1>
    <audio ref="beepSound" :src="audioSrc"></audio>
    <div class="qr-code-scanner" style="display: flex; justify-content: center;">
      <div style="height: 500px;">
        <video ref="video" autoplay playsinline></video>

        <div class="scan-overlay">
          <div class="scan-box">
            <div class="scan-box2"></div>
            <div class="scan-line"></div>
            <!-- 扫描成功提示 -->
            <div v-if="qrCodeData" class="scan-success">
              <span class="icon">✔️</span>
              <span>扫描成功</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import jsQR from "jsqr";

export default {
  data() {
    return {
      qrCodeData: null,
      videoStream: null,
      audioSrc: process.env.VUE_APP_BASEURL + "/files/QR-bi.mp3",
    };
  },
  mounted() {
    this.initCamera();
  },
  methods: {
    async initCamera() {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          video: {
            facingMode: "environment",
            width: { ideal: 1280 },
            height: { ideal: 720 },
          },
        });
        this.videoStream = stream;
        const video = this.$refs.video;
        video.srcObject = stream;
        await video.play();
        this.scanQRCode();
      } catch (error) {
        console.error("无法访问摄像头:", error);
        alert("无法访问摄像头，请确保已授予权限。");
      }
    },

    scanQRCode() {
      const video = this.$refs.video;
      const canvas = document.createElement("canvas");
      const context = canvas.getContext("2d");

      const scanFrame = () => {
        if (!this.videoStream || video.readyState !== video.HAVE_ENOUGH_DATA) {
          requestAnimationFrame(scanFrame);
          return;
        }

        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        context.drawImage(video, 0, 0, canvas.width, canvas.height);

        const imageData = context.getImageData(0, 0, canvas.width, canvas.height);
        const code = jsQR(imageData.data, imageData.width, imageData.height, {
          inversionAttempts: "attemptBoth",
        });

        if (code) {
          // this.stopCamera();
          console.log("二维码数据:", code);
          this.handleQRCodeData(code.data);
          requestAnimationFrame(scanFrame);
        } else {
          requestAnimationFrame(scanFrame);
        }
      };
      requestAnimationFrame(scanFrame);
    },

    handleQRCodeData(data) {
      console.log("二维码数据:", data);
      // 播放提示音
      const audio = this.$refs.beepSound;
      if (audio) {
        audio.play().catch(error => {
          console.error("无法播放提示音:", error);
        });
      }
      this.$emit("getPrivateKey", data);
      this.qrCodeData = data
      // 3 秒后隐藏提示
      setTimeout(() => {
        this.qrCodeData = null;
      }, 3000);
    },

    stopCamera() {
      this.videoStream?.getTracks().forEach(track => track.stop());
    },
  },
  beforeDestroy() {
    this.stopCamera();
  },
};
</script>

<style scoped>
video {
  width: 500px;
  height: 500px;
  object-fit: cover;
  border-radius: 8px;
}

.scan-overlay {
  position:relative;
  top: -400px;
  width: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
  pointer-events: none;
}

.scan-box {
  width: 60%;
  max-width: 300px;
  height: 300px;
  border: none;
  position: relative;
}

.scan-box::before,
.scan-box::after,
.scan-box2::before,
.scan-box2::after {
  content: '';
  position: absolute;
  width: 30px;
  height: 30px;
  border: 3px solid #4CAF50;
}

/* 左上角 */
.scan-box::before {
  top: 0;
  left: 0;
  border-right: none;
  border-bottom: none;
}

/* 右上角 */
.scan-box::after {
  top: 0;
  right: 0;
  border-left: none;
  border-bottom: none;
}

/* 左下角 */
.scan-box2::before {
  bottom: 0;
  left: 0;
  border-right: none;
  border-top: none;
}

/* 右下角 */
.scan-box2::after {
  bottom: 0;
  right: 0;
  border-left: none;
  border-top: none;
}

.scan-line {
  position: absolute;
  width: 100%;
  height: 2px;
  background: linear-gradient(to bottom, transparent 0%, #4CAF50 50%, transparent 100%);
  animation: scan 2s infinite linear;
}

@keyframes scan {
  0% { top: 0; opacity: 0; }
  50% { opacity: 1; }
  100% { top: 100%; opacity: 0; }
}

.scan-success {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: #4CAF50;
  background-color: rgba(255, 255, 255, 0.9);
  padding: 10px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  pointer-events: none;
}

.scan-success .icon {
  font-size: 32px;
  margin-bottom: 8px;
}
</style>