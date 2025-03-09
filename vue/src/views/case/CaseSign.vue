<template>
  <el-card class="container">
    <div class="header">
      <h2>病历签名</h2>
    </div>

    <el-row :gutter="24">
      <el-col :span="16">
        <div class="sign-area">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                <span style="display: inline-block; width: 200px;font-size: 16px;">环签名数据</span>
              </div>
            </el-col>
            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button plain type="primary" @click="sign()" v-if="user.role === 'DOCTOR'"
                  style="margin-bottom: 5px;">环签名</el-button>
                <el-button v-else type="primary" style="visibility: hidden;"> 占位 </el-button>
                <!-- <span><p style="color:red; display:inline; margin: 0px 0px 0px 10px">未检测到私钥 :(</p></span> -->
                <span>
                  <p style="color:green; display:inline; margin: 0px 0px 0px 10px; padding:0px;">已检测到私钥</p>
                </span>
              </div>
            </el-col>
          </el-row>
          <div class="grid-content bg-purple">
            <el-input type="textarea" :rows="6" readonly placeholder="请先签名" v-model="signData"
              style="margin-bottom: 5px;">
            </el-input>
          </div>
        </div>
        <el-row :gutter="24">
          <el-col :span="12">
            <!-- 公钥圆形环 -->
            <div class="card" ref="publicKeyRingChart" style="width: 350px; height: 350px;"></div>
          </el-col>

          <el-col :span="12">
            <div class="card" style="text-align: center;">
              <span
                style="display: inline-block; width: 200px;font-size: 18px; font-weight: bold;margin-bottom: 21px;">医生{{ doctorName }}的公钥</span>
              <el-input type="textarea" :rows="12" readonly placeholder="请先签名,然后点击左边公钥环，以查看医生个人公钥。" v-model="key"
                style="margin-bottom: 5px;">
              </el-input>
            </div>
          </el-col>
        </el-row>
      </el-col>

      <!-- TIMELINE -->
      <el-col :span="8">
        <el-timeline>
          <el-timeline-item timestamp="第一步" type="success" placement="top">
            <el-card>
              <h4>撰写病历</h4>
              <p>根据患者病情，开具合适的病历</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="第二步" type="info" placement="top">
            <el-card>
              <h4>病历签名</h4>
              <p>病历撰写完成后，请医生利用自己的私钥为病历进行签名</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="第三步" placement="top">
            <el-card>
              <h4>病历存档</h4>
              <p>您将使用患者的公钥对病历进行加密，并再次对加密的病历进行签名，之后，将病历上传至区块链，进行存档</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>

        <div class="button-container">
          <el-button type="primary" @click="gotoCaseEncrypt"> 病历存档</el-button>
        </div>
      </el-col>
    </el-row>

    <!-- <div class="public-keys">
      <div class="header" style="margin: 10px 0">
        <h2>环公钥组成信息</h2>
      </div>

      <div class="table-container">
        <el-table :data="pubs" border style="width: 100%;" >
          <el-table-column prop="name" align="center" label="姓名" width="180">
          </el-table-column>
          <el-table-column prop="key" align="center" label="公钥">
          </el-table-column>
        </el-table>
      </div>
    </div> -->

  </el-card>
</template>

<script>
import * as echarts from 'echarts';
import 'echarts/theme/macarons';

export default {
  data() {
    return {
      receivedData: {},
      params: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      signData: '',
      signPubKey: '',
      chartData: [],
      key: '', //公钥
      doctorName: '',
      chart: null,
    };
  },
  created() {
    this.receivedData = this.$route.query
    this.load()
  },
  methods: {
    load() {

    },
    sign() {
      this.$request.post('/keys/sign', this.receivedData).then(res => {
        this.signData = res.data.signData
        this.signPubKey = res.data.signPubKey
        const s = this.signPubKey.split(", ")
        const parsedData = s.map(line => {
          const parts = line.split(':');
          return {
            name: parts[0],
            publicKey: parts[1],
            value: 1,
          };
        });
        this.chartData = parsedData;
        this.chartNameData = parsedData.map(item => ({ name: item.name }));
        this.initChart();
      })
    },
    gotoCaseEncrypt() {
      this.$router.push({ name: "CaseEncrypt", query: this.receivedData })
    },
    initChart() {   //公钥圆形环
      this.chart = echarts.init(this.$refs.publicKeyRingChart, 'macarons');
      const option = {
        title: {
          text: '医生环公钥',
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}',
        },
        series: [
          {
            name: "",
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '20',
                fontWeight: 'bold',
              },
            },
            labelLine: {
              show: false,
            },
            data: this.chartData,
            itemStyle: {
              borderRadius: 5, 
              borderColor: '#fff', 
              borderWidth: 1, 
            },
          },
        ],
      };
      this.chart.setOption(option);
      // 监听点击事件
      this.chart.on('click', (params) => {
        // 根据点击的扇区名称找到对应的公钥
        const selectedData = this.chartData.find(item => item.name === params.name);
        if (selectedData) {
          this.key = selectedData.publicKey; // 更新文本框的公钥
          this.doctorName = params.name
        }
      });
    },
  }
};
</script>

<style scoped>
.header {
  margin-bottom: 20px;
}

.el-table {
  color: blue;
}

::v-deep .el-textarea__inner {
  color: blue;
}

.button-container {
  display: flex;
  justify-content: center;
}
</style>
