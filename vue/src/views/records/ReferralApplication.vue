<template>
  <div>
    <rl-row class="info-row">
      <span class="field-label">选择:</span>
      <el-select v-model="caseInfo" placeholder="请选择医院" >
        <div v-for="item in tableData">
          <el-option :label="item.userName+'-'+item.hospitalName" :value="item"></el-option>
        </div>
      </el-select>
        <span style="margin-left: 50px">姓名：</span>
      <span style="margin-left: 50px">{{caseInfo.userName}}</span>

    </rl-row>
    <div class="form-container">
      <div class="left-form">
        <div class="label">转出信息</div>
        <el-input placeholder="转出医院" v-model="caseInfo.hospitalName" :readonly="true" clearable></el-input>
        <el-input placeholder="转出医生" v-model="caseInfo.doctorName" :readonly="true" clearable></el-input>
      </div>

      <div class="right-form">
        <div class="label">转入信息</div>
        <el-select v-model="transferInHospital" placeholder="请选择医院" @change="loadByDoctor()" style="width: 500px;margin-bottom: 20px">
          <div v-for="item in infByHospital">
            <el-option :label="item.hospitalName" :value="item.id"></el-option>
          </div>
        </el-select>
        <el-select v-model="transferInDoctor" placeholder="请选择医生" style="width: 500px">
          <div v-for="item in infByDoctor">
            <el-option :label="item.name" :value="item.id"></el-option>
          </div>
        </el-select>
      </div>
    </div>

    <el-input type="textarea" placeholder="转院理由" v-model="transferReason" clearable :rows="4"></el-input>

    <el-button type="primary" style="margin-top: 10px;" @click="confirmTransfer">确定</el-button>
  </div>
</template>

<script>
export default {
  name: "ReferralApplication",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      name:"",
      transferOutHospital: '',
      transferOutDoctor: '',
      transferOutTime: null,
      transferInHospital: '',
      transferInDoctor: '',
      transferInTime: null,
      transferReason: '',
      caseInfo:[],
      tableData:[],
      infByHospital:[],
      infByDoctor:[],
      information:{}
    }
  },
  created() {
    this.loadByUser()
    this.loadByHospital()
    //this.loadByDoctor()
  },
  methods: {
    loadByUser(){
      this.$request.get('/record/selectAll',{ // 只能再就诊记录中选择要转诊的患者
        params:{
          doctorId:this.user.id
        }
      }).then(res => {
        this.tableData = res.data
      })
    },
    loadByDoctor() {
      let id=this.transferInHospital
      this.$request.get('/doctor/selectByH/'+id).then(res => {
        if (res.code === '200') {
          this.infByDoctor = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadByHospital(){
      this.$request.get('/hospital/selectAll').then(res=>{
        this.infByHospital=res.data
      })
    },
    /**
     * 转诊申请
     */
    confirmTransfer() {
      this.information.userId=this.caseInfo.userId
      this.information.outHospitalId=this.caseInfo.hospitalId
      this.information.outDoctorId=this.caseInfo.doctorId
      this.information.inHospitalId=this.transferInHospital
      // this.information.inDoctorId=this.transferInDoctor // 医生由管理员分配
      this.information.why=this.transferReason
      // this.information.result="待接收"
      let data = JSON.parse(JSON.stringify(this.information))
      this.$request.post('/referal/add', data).then(res => {
        if (res.code === '200') {
          // 自动跳转
          const countdownSeconds = 3;
          let countdown = countdownSeconds;
          const countdownInterval = setInterval(() => {
            if (countdown > 0) {
              this.$message.info(`申请成功，${countdown}秒后将跳转页面...`);
              countdown--;
            } else {
              clearInterval(countdownInterval);
              this.$router.push('/referralRecord');
            }
          }, 1000);
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.form-container {
  display: flex;
  justify-content: space-between;
}

.left-form, .right-form {
  flex: 1;
  margin-right: 20px;
}

.right-form {
  margin-right: 0;
}

.label {
  margin-bottom: 10px;
  font-weight: bold;
}

.el-input, .el-date-picker {
  margin-bottom: 20px;
}

.el-textarea {
  margin-top: 20px;
}
</style>
