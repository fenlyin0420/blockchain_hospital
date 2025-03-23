<template>
    <div>
      <div class="title">
        <span v-if="percentage < 100">正在智能匹配{{ hospital }}</span>
        <div v-else>
          <div class="title">
            <span>已匹配到以下医院：</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12" v-for="(item, index) in hospitalData" :key="index">
              <div class="hospital-item">
                <p><strong>医院名称：</strong>{{ item.hospital }}</p>
                <p><strong>专科匹配度：</strong>{{ item.matched }}</p>
                <p><strong>当前候诊人数：</strong>{{ item.number }}</p>
                <p><strong>交通便利指数：</strong>{{ item.transportation }}</p>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
      <el-progress
        v-if="percentage < 100" 
        :percentage="percentage"
        :color="progressColor"
        :stroke-width="20"
        :text-inside="true"
        :format="formatPercentage"
      ></el-progress>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        hospitalData: [], // 存储解析后的医院数据
        hospital: "", // 当前显示的医院名称
        percentage: 0, // 当前进度
        gradientColors: [
          "#ff2e2e", "#f73633", "#ef3e39", "#e7463e", "#e04e44", "#d85649", "#d05e4f",
          "#c86654", "#c06e59", "#b8765f", "#b17e64", "#a9866a", "#a18e6f", "#999775",
          "#919f7a", "#89a77f", "#81af85", "#7ab78a", "#72bf90", "#6ac795", "#62cf9a",
          "#5ad7a0", "#52dfa5", "#4be7ab", "#43efb0", "#3bf7b6", "#33ffbb"
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
    },
    mounted() {
      this.startProgress();
      this.load();
    },
    methods: {
      load() {
        this.$blockRequest.post("/getHospitalData").then((res) => {
          if (res.data.code === "200") {
            // 检查 returnObject 是否存在
            if (res.data.data && res.data.data.returnObject && res.data.data.returnObject.length > 0) {
              // 将 returnObject 的第一个元素（字符串）按医院信息分割
              const hospitalsInfo = res.data.data.returnObject[0].split("\n\n");
              // 解析每条医院信息并存入 hospitalData 数组
              this.hospitalData = hospitalsInfo.map(info => {
                // 去掉每条信息首尾的换行符
                const trimmedInfo = info.trim();
                // 按行分割每条医院信息
                const lines = trimmedInfo.split("\n");
                // 提取每行的信息
                const hospital = lines[0]; // 医院名称
                const matched = lines[1].split(": ")[1]; // 专科匹配度
                const number = lines[2].split(": ")[1]; // 当前候诊人数
                const transportation = lines[4].split(": ")[1]; // 交通便利指数
                // 返回一个对象
                return {
                  hospital,
                  matched,
                  number,
                  transportation
                };
              });
            } else {
              console.error("返回数据中没有 returnObject 或数据为空");
            }
          } else {
            console.error("请求失败，状态码：", res.data.code);
          }
        }).catch((error) => {
          console.error("请求发生错误：", error);
        });
      },
      startProgress() {
        let interval = setInterval(() => {
          if (this.percentage < 100) {
            this.percentage += 1;
            this.updateHospital(); // 更新医院名称
          } else {
            clearInterval(interval);
          }
        }, 60); // 每隔 0.06 秒增加 1%
      },
      updateHospital() {
        // 确保 hospitalData 不为空
        if (this.hospitalData.length > 0) {
          this.hospital = this.hospitalData[this.currentHospitalIndex].hospital;
        } else {
          this.hospital = "加载中..."; // 或者其他默认值
        }
        // 切换到下一个医院名称
        this.currentHospitalIndex = (this.currentHospitalIndex + 1) % this.hospitalData.length;
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
        return `#${this.componentToHex(r)}${this.componentToHex(g)}${this.componentToHex(b)}`;
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
  /* 设置进度条文本样式 */
  ::v-deep .el-progress__text {
      color: black;
      /* 设置文本颜色为黑色 */
      font-weight: bold;
      /* 设置文本加粗 */
  }
  
  .title {
      margin-bottom: 10px;
  }
  
  .hospital-item {
      margin-bottom: 20px;
      border: 1px solid #ccc;
      padding: 10px;
      border-radius: 5px;
  }
  </style>