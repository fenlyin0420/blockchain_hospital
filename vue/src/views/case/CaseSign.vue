<template>
  <el-card class="container">
    <div class="header">
      <h2>病历签名</h2>
    </div>
    <el-row :gutter="24">
      <el-col :span="8">
        <div style="margin: 0 0 20px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px  0 25px">
                <span style="display: inline-block; width: 200px;font-size: 16px;">环签名数据</span>
              </div>
            </el-col>
            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button plain type="primary" @click="sign()" v-if="user.role === 'DOCTOR'">签名</el-button>
                <el-button v-else type="primary" style="visibility: hidden;"> 占位 </el-button>
                <!-- <span><p style="color:red; display:inline; margin: 0px 0px 0px 10px">未检测到私钥 :(</p></span> -->
                <span><p style="color:green; display:inline; margin: 0px 0px 0px 10px">已检测到私钥</p></span>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="grid-content bg-purple">
          <el-input type="textarea" :rows="6" readonly placeholder="请输入内容"
            v-model="signData">
          </el-input>
        </div>
      </el-col>

      <!-- 签名信息 --> 
      <el-col :span="8">
        <div style="margin: 0 0 20px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                <span style="display: inline-block; width: 200px; font-size: 16px;">环签名信息</span>
              </div>
            </el-col>

            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button  type="primary" style="visibility: hidden;" >占位 </el-button>
              </div>
            </el-col>
          </el-row>

        </div>
        <div class="grid-content bg-purple-light">
          <el-input type="textarea" :rows="6" readonly placeholder="请输入内容"
            v-model="signKey">
          </el-input>
        </div>
      </el-col>

      <!-- 验签结果 --> 
      <el-col :span="8">
        <div style="margin: 0 0 20px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                <sapn style="display: inline-block; width: 200px; font-size: 16px;">验签结果</sapn>
              </div>
            </el-col>

            <el-col :span="14">
              <div class="grid-content bg-purple-light">
              <!-- 验签按钮 -->
                <el-button plain type="primary" @click="verifySign()" v-if="user.role === 'USER'">验签</el-button>
                <el-button v-else type="primary" style="visibility: hidden;"> 占位 </el-button>
              </div>
            </el-col>
          </el-row>
        </div>

        <div>
          <el-input type="textarea" :rows="6" readonly placeholder="请输入内容"
            v-model="signResult">
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div class="header" style="margin: 20px 0">
      <h2>环公钥组成信息</h2>
    </div>

    <el-table :data="pubs" border style="width: 100%;" >
      <el-table-column prop="name" label="姓名" width="180">
      </el-table-column>
      <el-table-column prop="key" label="公钥">
      </el-table-column>
    </el-table>

  </el-card>
</template>

<script>
export default {
  data() {
    return {
      receivedData: [],
      params: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      fits: 'fill',
      signData: '',
      signKey: '',
      signResult: '',
      signPubKey: ''
    };
  },
  created() {
    this.receivedData = this.$route.query
    this.load()
  },
  computed: {
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
    /**
     * 签名
     */
    sign() {
      this.params.name = this.receivedData.userName
      this.params.id = this.receivedData.id
      this.$request.post('/keys/sign', this.params).then(res => {
        if (res.code === '200') {
          this.signData = res.data.signData
          this.signKey = res.data.signKey
          this.signPubKey = res.data.signPubKey

          // 自动跳转
          const countdownSeconds = 3;
          let countdown = countdownSeconds;
          const countdownInterval = setInterval(() => {
            if (countdown > 0) {
              this.$message.info(`签名成功，${countdown}秒后将跳转页面...`);
              countdown--;
            } else {
              clearInterval(countdownInterval);
              this.$router.push('/doctorReserve');
            }
          }, 1000);
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    verifySign() {
      this.params.role = this.user.role
      this.params.name = this.receivedData.name
      this.params.timestamp = this.receivedData.timestamp
      this.params.signKey = this.receivedData.signKey
      this.$request.post('/keys/verifySign', this.params).then(res => {
        if (res.code === '200') {
          this.receivedData.signResult = res.data.message
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
  height: 100%;
  overflow-y: scroll;
}

.header {
  margin-bottom: 20px;
}

.el-table {
  color: blue;
}
::v-deep .el-textarea__inner {
  color: blue;
}

</style>
