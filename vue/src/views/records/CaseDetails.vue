<template>
  <el-card class="case-details">
    <div class="header">
      <h2>病例详情</h2>
    </div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form label-width="120px">
          <!-- <el-form-item label="医院名称">
            <span>{{ receivedData.hospitalName }}</span>
          </el-form-item> -->
          <el-form-item label="患者姓名" >
            <span>{{ receivedData.name }}</span>
          </el-form-item>
          <!-- <el-form-item label="主治医生姓名">
            <span>{{ receivedData.doctorName }}</span>
          </el-form-item> -->
        </el-form>
        <el-button type="primary" @click="decryptAdviceAndDrug">解密</el-button>
      </el-col>
      <el-col :span="16">
        <el-form label-width="120px">
          <el-form-item label="医嘱信息">
            <el-input
                type="textarea"
                :autosize="{ minRows: 4, maxRows: 4}"
                placeholder="请输入内容"
                v-model="receivedData.advice">
            </el-input>
          </el-form-item>
          <el-form-item label="药品信息">
            <el-table :data="drug" style="width: 100%" height="200" border >
              <el-table-column prop="name" label="药品名称"></el-table-column>
              <el-table-column prop="dose" label="数量"></el-table-column>
              <el-table-column prop="frequency" label="用法用量"></el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="8">
        <div style="margin: 0 0 20px 0">

          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                环签名数据
              </div>
            </el-col>
            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button plain type="primary" @click="sign()" v-if="user.role==='DOCTOR'">签名</el-button>
              </div>
            </el-col>
          </el-row>

        </div>
        <div class="grid-content bg-purple">
          <el-input
              type="textarea"
              :autosize="{ minRows: 4, maxRows: 8}"
              placeholder="请输入内容"
              v-model="receivedData.signData">
          </el-input>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="margin: 0 0 29px 0">

          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                环签名信息
              </div>
            </el-col>
          </el-row>

        </div>
        <div class="grid-content bg-purple-light">
          <el-input
              type="textarea"
              :autosize="{ minRows: 4, maxRows: 8}"
              placeholder="请输入内容"
              v-model="receivedData.signKey">
          </el-input>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="margin: 0 0 20px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                验签结果
              </div>
            </el-col>
            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button plain type="primary" @click="verifySign()" v-if="user.role==='USER'">验签</el-button>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="grid-content bg-purple-light">
          <el-input
              type="textarea"
              :autosize="{ minRows: 4, maxRows: 8}"
              placeholder="请输入内容"
              v-model="receivedData.signResult">
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div class="header" style="margin: 20px 0">
      <h2>环公钥组成信息</h2>
    </div>

    <div v-if="pubs.length!==0">
      <el-table :data="pubs" height="250" border style="width: 100%">
        <el-table-column
            prop="name"
            label="姓名"
            width="180">
        </el-table-column>
        <el-table-column
            prop="key"
            label="公钥">
        </el-table-column>
      </el-table>
    </div>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      receivedData:[],
      params:{},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      pubs:{}
    };
  },
  created() {
    const queryData = this.$route.query.data;
    if (queryData) {
      this.receivedData = JSON.parse(decodeURIComponent(queryData));
    }
    else{
      this.receivedData = this.$route.params.inform;
      console.log(this.receivedData)
    }
    this.load()
  },
  computed: {
    drug() {
      const medicationString = this.receivedData.drug;
      // 拆分字符串为每一行
      const lines = medicationString.split('\n');
      // 将每一行拆分为药物信息对象
      return lines.map(line => {
        const parts = line.split(' ');
        return {
          name: parts[0],
          dose: parts[1],
          frequency: parts[2]
        };
      });
    }
  },
  methods: {
    load() {
      if(this.receivedData.length){
        this.$router.push("/caseList")
      }

      if(this.receivedData.signPubKey!==null||this.receivedData.signPubKey!==""){
        const s = this.receivedData.signPubKey.split(",")
        const ss = s.map(line => {
          const parts = line.split(':');
          return {
            name: parts[0],
            key: parts[1],
          };
        });
        this.pubs=ss
      }
    },
    /**
     * 数据解密
     */
    decryptAdviceAndDrug() {
      let params = {
        name: this.receivedData.name,
        advice: this.receivedData.advice,
        drug: this.receivedData.drug
      }
      this.$request.post('keys/decrypt', params).then(res => {
        if (res.code === '200'){
          this.receivedData.advice = res.data.advice
          this.receivedData.drug = res.data.drug
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    sign(){
      console.log(this.receivedData)
      this.params.role = this.user.role
      this.params.id = this.receivedData.id
      this.params.name = this.receivedData.name
      this.$request.post('/keys/sign', this.receivedData).then(res => {
        if (res.code === '200') {
          // this.$message.success('成功')
          this.receivedData.signData=res.data.signData
          this.receivedData.signKey=res.data.signKey
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    verifySign(){
      this.params.role=this.user.role
      this.params.id=this.receivedData.id
      this.params.name=this.receivedData.name
      this.params.signKey=this.receivedData.signKey
      this.$request.post('/keys/verifySign', this.receivedData).then(res => {
        if (res.code === '200') {
          // this.$message.success('成功')
          this.receivedData.signResult=res.data.message
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
};
</script>

<style scoped>
.case-details {
  margin: 20px;
}

.header {
  margin-bottom: 20px;
}
</style>
