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
                <el-button plain type="primary" @click="sign()" v-if="user.role === 'DOCTOR'">环签名</el-button>
                <el-button v-else type="primary" style="visibility: hidden;"> 占位 </el-button>
                <!-- <span><p style="color:red; display:inline; margin: 0px 0px 0px 10px">未检测到私钥 :(</p></span> -->
                <span ><p style="color:green; display:inline; margin: 0px 0px 0px 10px; padding:0px;">已检测到私钥</p></span>
              </div>
            </el-col>
          </el-row>
          <div class="grid-content bg-purple">
            <el-input type="textarea" :rows="6" readonly placeholder="请先签名"
              v-model="signData">
            </el-input>
          </div>
        </div>
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
    <div class="public-keys">
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
    </div>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      params: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      signData: '',
      signPubKey: '',
    };
  },
  created() {
    console.log("CaseSign receivedData", this.receivedData)
    this.load()
  },
  computed: {
    receivedData() {
      return this.$store.state.traverseData
    },
    pubs() {
      if (this.signPubKey != '') {
        const s = this.signPubKey.split(",")
        const ss = s.map(line => {
          const parts = line.split(':');
          return {
            name: parts[0],
            key: parts[1],
          };
        });
        return ss
      }
    }
  },
  methods: {
    load() {

    },
    sign() {
      this.$request.post('/keys/sign', this.receivedData).then(res => {
        this.signData = res.data.signData
        this.signPubKey = res.data.signPubKey
      })
    },
    gotoCaseEncrypt() {
      this.$router.push({ name: "CaseEncrypt", state: this.receivedData })
    }
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
