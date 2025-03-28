<template>
  <div class="container">
    <div class="title">
      <span v-if="percentage < 100">正在智能匹配医院</span>
      <div v-else>
        <div class="title">
          <span>已匹配到以下医院：</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="hospital-item">
              <p><strong>医院名称：</strong>{{ hospitalData[0].hospital }}</p>
              <p><strong>专科匹配度：</strong>{{ hospitalData[0].matched }}</p>
              <p><strong>当前候诊人数：</strong>{{ hospitalData[0].number }}</p>
              <p><strong>交通便利指数：</strong>{{ hospitalData[0].transportation }}</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div v-if="percentage < 100" class="progress-container">
      <div class="influence-factors">
        <p>
          <strong>医院名称：</strong>{{ hospital }}
        </p>
        <p>
          <strong>专科匹配度：</strong>{{ currentHospital.matched }}
        </p>
        <p>
          <strong>当前候诊人数：</strong>{{ currentHospital.number }}
        </p>
        <p>
          <strong>交通便利指数：</strong>{{ currentHospital.transportation }}
        </p>
      </div>
      <el-progress
        :percentage="percentage"
        :color="progressColor"
        :stroke-width="20"
        :text-inside="true"
        :format="formatPercentage"
      ></el-progress>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hospitalData: [
        {
          hospital: "医院A",
          matched: "90%",
          number: "10",
          transportation: "80%",
        },
        {
          hospital: "医院B",
          matched: "85%",
          number: "15",
          transportation: "75%",
        },
        {
          hospital: "医院C",
          matched: "80%",
          number: "20",
          transportation: "90%",
        },
        {
          hospital: "医院D",
          matched: "75%",
          number: "25",
          transportation: "60%",
        },
        {
          hospital: "医院E",
          matched: "95%",
          number: "5",
          transportation: "85%",
        },
        {
          hospital: "医院F",
          matched: "88%",
          number: "12",
          transportation: "70%",
        },
      ], // 存储静态医院数据
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
    this.startProgress();
  },
  methods: {
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
        this.hospital = this.hospitalData[this.currentHospitalIndex].hospital;
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
  },
};
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}

.hospital-item {
  background-color: #fff;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 20px;
}

.progress-container {
  position: relative;
  margin-top: 20px;
}

.progress-container .influence-factors {
  position: relative;
  background-color: #fff;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.progress-container .influence-factors p {
  margin: 0;
  line-height: 1.5;
  font-size: 14px;
  color: #333;
}

::v-deep .el-progress__text {
  color: black;
  font-weight: bold;
}
</style>