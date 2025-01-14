<template>
  <el-card class="container">
    <div class="header">
      <h2>病历签名</h2>
    </div>
    <el-row :gutter="24">
      <el-col :span="8">
        <div style="margin: 0 0 10px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                <span style="display: inline-block; width: 200px;font-size: 16px;">环签名数据</span>
              </div>
            </el-col>
            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button plain type="primary" @click="sign()" v-if="user.role === 'DOCTOR'">签名</el-button>
                <el-button v-else type="primary" style="visibility: hidden;"> 占位 </el-button>
                <!-- <span><p style="color:red; display:inline; margin: 0px 0px 0px 10px">未检测到私钥 :(</p></span> -->
                <span ><p style="color:green; display:inline; margin: 0px 0px 0px 10px; padding:0px;">已检测到私钥</p></span>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="grid-content bg-purple">
          <el-input type="textarea" :rows="2" readonly placeholder="请输入内容"
            v-model="signData">
          </el-input>
        </div>
      </el-col>

      <!-- 签名信息 --> 
      <el-col :span="8">
        <div style="margin: 0 0 10px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                <span style="display: inline-block; width: 200px; font-size: 16px;">环签名信息</span>
              </div>
            </el-col>

            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button  type="primary" style="opacity:0" >占位 </el-button>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="grid-content bg-purple-light">
          <el-input type="textarea" :rows="2" readonly placeholder="请输入内容"
            v-model="signKey">
          </el-input>
        </div>
      </el-col>

      <!-- 验签结果 --> 
      <el-col :span="8">
        <div style="margin: 0 0 10px 0">
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
          <el-input type="textarea" :rows="2" readonly placeholder="请输入内容"
            v-model="signResult">
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div class="header" style="margin: 10px 0">
      <h2>签名数据区块链地址</h2>
    </div>
    <el-table :data="blockInfo" border style="width: 100%;" >
      <el-table-column prop="QR" align="center" label=" 二维码" width="180">
        <template slot-scope="scope">
          <img :src="scope.row.QR" width="100px" height="100px">
        </template>
      </el-table-column>
      <el-table-column prop="blockHash" align="center" label="区块链存储地址">
      </el-table-column>
    </el-table>

    <div class="header" style="margin: 10px 0">
      <h2>环公钥组成信息</h2>
    </div>

    <el-table :data="pubs" border style="width: 100%;" >
      <el-table-column prop="name" align="center" label="姓名" width="180">
      </el-table-column>
      <el-table-column prop="key" align="center" label="公钥">
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
      signPubKey: '',
      blockInfo: [{ QR: '', blockHash: 'NULL' }]
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
    upChain() {
      if (true){
        this.blockInfo[0].blockHash = 'BPcYz3sejN3rkSAmVLQWxhx6Hr/Tf4Q37YR+raOPeTb9e/DzVOHkSAmVLQWxhxR+raOPeTb9e/D'
        this.$request.get('/files/generateQR', {
          params: {
            seed: this.blockInfo[0].blockHash
          }
        }).then(res => {
          if (res.code === '200'){
            this.blockInfo[0].QR = res.data
          }
        })
        return 
      }
      // 1. 获取签名数据的区块链地址

      // 2. 根据地址生成二维码，得到二维码的url
    },
    /**
     * 签名
     */
    sign() {
      // 病历 id
      this.params.id = this.receivedData.id
      this.$request.post('/keys/sign', this.params).then(res => {
        if (res.code === '200') {
          this.signData = res.data.signData
          this.signKey = res.data.signKey
          this.signPubKey = res.data.signPubKey
          this.upChain()
          // 自动跳转
          // const countdownSeconds = 3;
          // let countdown = countdownSeconds;
          // const countdownInterval = setInterval(() => {
          //   if (countdown > 0) {
          //     this.$message.info(`签名成功，${countdown}秒后将跳转页面...`);
          //     countdown--;
          //   } else {
          //     clearInterval(countdownInterval);
          //     this.$router.push('/doctorReserve');
          //   }
          // }, 1000);
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    verifySign() {
      this.params.id = this.receivedData.id
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
