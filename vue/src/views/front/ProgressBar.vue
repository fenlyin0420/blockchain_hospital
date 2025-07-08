<template>
  <div class="container">
    <!-- 标题部分增加动态图标 -->
    <div class="title">
      <template v-if="percentage < 100">
        <i class="el-icon-loading" style="margin-right: 8px;"></i>
        <span class="gradient-text">正在智能匹配医院...</span>
      </template>
      <template v-else>
        <div class="result-header">
          <i class="el-icon-success" style="color: #67C23A; margin-right: 8px;"></i>
          <span class="gradient-text">已匹配到以下医院：</span>
        </div>
        <!-- 增加卡片悬浮动画 -->
        <transition name="el-zoom-in-center">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :md="24">
              <div class="hospital-card">
                <div class="card-header">
                  <span class="hospital-name">{{ hospitalData[1].hospitalName }}</span>
                  <el-tag type="success" effect="dark">推荐</el-tag>
                </div>
                <div class="card-content">
                  <div class="info-item">
                    <i class="el-icon-data-analysis"></i>
                    <span class="label">专科匹配度：</span>
                    <span class="value highlight">{{ hospitalData[1].matched }}</span>
                  </div>
                  <div class="info-item">
                    <i class="el-icon-user"></i>
                    <span class="label">当前候诊人数：</span>
                    <span class="value">{{ hospitalData[1].number }}</span>
                  </div>
                  <div class="info-item">
                    <i class="el-icon-map-location"></i>
                    <span class="label">交通便利指数：</span>
                    <span class="value highlight">{{ hospitalData[1].transportation }}</span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </transition>
      </template>
    </div>

    <!-- 进度条区域 -->
    <transition name="el-fade-in">
      <div v-if="percentage < 100" class="progress-wrapper">
        <!-- 动态参数卡片 -->
        <div class="dynamic-card">
          <div class="dynamic-item">
            <span class="icon">🏥</span>
            <span class="text">{{ hospital }}</span>
          </div>
          <div class="dynamic-item">
            <span class="icon">📊</span>
            <span class="text">{{ currentHospital.matched }}</span>
          </div>
          <div class="dynamic-item">
            <span class="icon">👥</span>
            <span class="text">{{ currentHospital.number }}人</span>
          </div>
          <div class="dynamic-item">
            <span class="icon">🚗</span>
            <span class="text">{{ currentHospital.transportation }}</span>
          </div>
        </div>

        <!-- 进度条增加光晕效果 -->
        <el-progress 
          :percentage="percentage"
          :color="progressColor"
          :stroke-width="24"
          :text-inside="true"
          class="glow-progress"
        >
          <span class="progress-text">{{ percentage }}%</span>
        </el-progress>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hospitalData: [
      ], 
      hospital: "", // 当前显示的医院名称
      percentage: 0, // 当前进度
      gradientColors: [
        "#ff2e2e",
        "#f73633",
        "#ef3e39",
        "#e7463e",
        "#e04e44",
        "#d85649",
        "#d05e4f",
        "#c86654",
        "#c06e59",
        "#b8765f",
        "#b17e64",
        "#a9866a",
        "#a18e6f",
        "#999775",
        "#919f7a",
        "#89a77f",
        "#81af85",
        "#7ab78a",
        "#72bf90",
        "#6ac795",
        "#62cf9a",
        "#5ad7a0",
        "#52dfa5",
        "#4be7ab",
        "#43efb0",
        "#3bf7b6",
        "#33ffbb",
      ],
      currentHospitalIndex: 0, // 当前医院索引
      lastUpdatedPercentage: 0, // 上一次更新医院名称时的进度
    };
  },
  computed: {
    // 根据当前进度返回对应的颜色
    progressColor() {
      const totalColors = this.gradientColors.length;
      const colorIndex = Math.floor((this.percentage / 100) * (totalColors - 1));
      const nextColorIndex = Math.min(colorIndex + 1, totalColors - 1);

      const currentColor = this.hexToRgb(this.gradientColors[colorIndex]);
      const nextColor = this.hexToRgb(this.gradientColors[nextColorIndex]);
      const ratio = (this.percentage / 100) * (totalColors - 1) - colorIndex;

      const interpolatedColor = this.interpolateColor(currentColor, nextColor, ratio);
      return this.rgbToHex(interpolatedColor);
    },
    // 当前匹配的医院数据
    currentHospital() {
      return this.hospitalData[this.currentHospitalIndex];
    },
  },
  mounted() {
    this.loadHospital();
  },
  methods: {
    loadHospital() {
      this.$request('/hospital/selectAll').then(res => {
        if (res.code === '200') {
          this.hospitalData = res.data;
          this.hospitalData.map(item => {
            item.matched = this.getPercentage(2);
            item.number = this.getInt(10, 50);
            item.transportation = this.getPercentage(2);
          })
          this.hospitalData[1].matched = '98.34%'
          this.hospitalData[1].number = 26
          this.hospitalData[1].transportation = '92.56%'
          this.startProgress();
        }
      })
    },
    startProgress() {
      let interval = setInterval(() => {
        if (this.percentage < 100) {
          this.percentage += 1;
          this.updateHospital(); // 更新医院名称
        } else {
          clearInterval(interval);
        }
      }, 100); // 每隔 0.06 秒增加 1%
    },
    updateHospital() {
      // 确保 hospitalData 不为空
      if (this.hospitalData.length > 0) {
        this.hospital = this.hospitalData[this.currentHospitalIndex].hospitalName;
      } else {
        this.hospital = "加载中..."; // 或者其他默认值
      }
      // 切换到下一个医院名称
      this.currentHospitalIndex =
        (this.currentHospitalIndex + 1) % this.hospitalData.length;
    },
    // 将十六进制颜色转换为 RGB 对象
    hexToRgb(hex) {
      const r = parseInt(hex.slice(1, 3), 16);
      const g = parseInt(hex.slice(3, 5), 16);
      const b = parseInt(hex.slice(5, 7), 16);
      return { r, g, b };
    },
    // 将 RGB 对象转换为十六进制颜色
    rgbToHex({ r, g, b }) {
      return `#${this.componentToHex(r)}${this.componentToHex(g)}${this.componentToHex(
        b
      )}`;
    },
    // 将单个颜色分量（0-255）转换为两位十六进制字符串
    componentToHex(c) {
      const hex = c.toString(16);
      return hex.length === 1 ? `0${hex}` : hex;
    },
    // 插值计算两个颜色之间的过渡颜色
    interpolateColor(color1, color2, ratio) {
      const r = Math.round(color1.r + ratio * (color2.r - color1.r));
      const g = Math.round(color1.g + ratio * (color2.g - color1.g));
      const b = Math.round(color1.b + ratio * (color2.b - color1.b));
      return { r, g, b };
    },
    // 自定义进度条文本格式
    formatPercentage(percentage) {
      return `${percentage}%`;
    },
    getInt(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
    getPercentage(precision = 2) {
      const random = Math.random() * 100;
      return random.toFixed(precision) + "%";
    }
  },
};
</script>

<style scoped>
/* 容器优化 */
.container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background: linear-gradient(145deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.result-header {
  margin-bottom: 10px;
}

/* 渐变文字效果 */
.gradient-text {
  background: linear-gradient(45deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: 600;
  font-size: 1.5rem;
}

/* 医院卡片设计 */
.hospital-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.hospital-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 1.2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #eee;
}

.hospital-name {
  font-size: 1.3rem;
  font-weight: 600;
  color: #303133;
  margin-right: 1rem;
}

/* 信息项样式 */
.info-item {
  display: flex;
  align-items: center;
  margin: 0.8rem 0;
  font-size: 1rem;
}

.info-item i {
  margin-right: 0.8rem;
  font-size: 1.2rem;
  color: #409EFF;
}

.highlight {
  color: #67C23A;
  font-weight: 500;
}

/* 动态参数卡片 */
.dynamic-card {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 1rem;
  background: rgba(255, 255, 255, 0.95);
  padding: 1.5rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.08);
}

.dynamic-item {
  display: flex;
  align-items: center;
  padding: 0.8rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.icon {
  font-size: 1.4rem;
  margin-right: 0.8rem;
}

/* 进度条光晕效果 */
.glow-progress {
  position: relative;
}

.glow-progress::after {
  content: "";
  position: absolute;
  top: -5px;
  left: -5px;
  right: -5px;
  bottom: -5px;
  background: inherit;
  filter: blur(15px);
  opacity: 0.3;
  z-index: -1;
}

.progress-text {
  font-size: 1.2rem;
  letter-spacing: 1px;
  font-weight: bold;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .container {
    margin: 1rem;
    padding: 1.2rem;
  }
  
  .dynamic-card {
    grid-template-columns: 1fr;
  }
}
</style>