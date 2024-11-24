<template>
  <div>
    <div class="form-container">
      <div class="left-form">
        <div class="label">转诊患者</div>
        <el-select v-model="caseInfo" placeholder="请选择患者" style="width:80%; margin-bottom: 20px;">
          <div v-for="item in tableData">
            <el-option :label="item.userName" :value="item"></el-option>
          </div>
        </el-select>

        <div class="label">转出信息</div>
        <el-input placeholder="转出医院" v-model="caseInfo.hospitalName" :readonly="true" clearable style="width:80%;"></el-input>
        <el-input placeholder="转出医生" v-model="caseInfo.doctorName" :readonly="true" clearable style="width:80%;"></el-input>
        <div class="label">转入信息</div>
        <el-select v-model="transferInHospital" placeholder="请选择医院" @change="loadByDoctor()" style="width: 80%;margin-bottom: 20px">
          <div v-for="item in infByHospital">
            <el-option :label="item.hospitalName" :value="item.id"></el-option>
          </div>
        </el-select>
        
        <el-input type="textarea" placeholder="转院理由" v-model="transferReason" clearable :rows="4" style="width:80%;"></el-input><br/>
        <el-button type="primary" style="margin-top: 10px; position:absolute; right:50%" @click="confirmTransfer">确定</el-button>
      </div>

      <div class="right-form">
        <el-form label-width="100px" style="margin-top: 20px;">
          <div class="label">诊断结果</div>
          <el-form-item>
            <el-input  :autosize="{ minRows: 4, maxRows: 4 }"
              v-model="advice">
            </el-input>
          </el-form-item>
          <div class="label">药品信息</div>
          <el-form-item>
            <el-table :data="drug" style="width: 100%"  border>
              <el-table-column prop="name" label="药品名称"></el-table-column>
              <el-table-column prop="dose" label="数量"></el-table-column>
              <el-table-column prop="frequency" label="用法用量"></el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
      </div>
    </div>

    
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
      information:{},
      drug:[
        {        
        name: "999感冒灵颗粒",
        dose: 3,
        frequency: "一日3次"
        },
        {        
        name: "盐酸左氧氟沙星片",
        dose: 3,
        frequency: "一日3次"
        },
        {        
        name: "健胃消食片",
        dose: 3,
        frequency: "一日3次"
        },
      ],
      advice: "多喝水"
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
        console.log(this.tableData)
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
