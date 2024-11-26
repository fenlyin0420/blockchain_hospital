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
        <el-input placeholder="转出医院" v-model="caseInfo.hospitalName" :readonly="true" clearable
          style="width:80%;"></el-input>
        <el-input placeholder="转出医生" v-model="caseInfo.doctorName" :readonly="true" clearable
          style="width:80%;"></el-input>
        <div class="label">转入信息</div>
        <el-select v-model="transferInHospital" placeholder="请选择医院" @change="loadByDoctor()"
          style="width: 80%;margin-bottom: 20px">
          <div v-for="item in infByHospital">
            <el-option :label="item.hospitalName" :value="item.id"></el-option>
          </div>
        </el-select>
        <el-autocomplete class="inline-input" type="textarea" v-model="transferReason" clearable :rows="4" style="width:80%;"
          :fetch-suggestions="querySearch" placeholder="转院理由" @select="handleSelect"></el-autocomplete>
        <br/>
        <el-button type="primary" style="margin-top: 10px; position:absolute; right:50%"
          @click="confirmTransfer">确定</el-button>

        <el-autocomplete type="textarea" v-model="transferReason" :fetch-suggestions="transferReasonComplete" clearable
          placeholder="请输入转诊原因" @select="handleSelect" style="width: 80%;display:block" />
        <el-button type="primary" style="margin-top: 10px; position: relative; left:400px"
          @click="confirmTransfer">确定</el-button>
      </div>

      <div class="right-form">
        <el-form label-width="0px" style="margin-top: 20px;">
          <div class="label">沟通记录表</div>
          <el-form-item>
            <el-input type="textarea" placeholder="患者承诺" v-model="advice" clearable :rows="4"
              style="width:90%;"></el-input>
          </el-form-item>
          <div class="label">患者签字</div>
          <el-form-item>
            <el-input type="textarea" placeholder="患者签字" v-model="signature" clearable :rows="4"
              style="width:90%;"></el-input>
            <!-- <el-table :data="drug" style="width: 100%"  border>
              <el-table-column prop="name" label="药品名称"></el-table-column>
              <el-table-column prop="dose" label="数量"></el-table-column>
              <el-table-column prop="frequency" label="用法用量"></el-table-column>
            </el-table> -->
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
      name: "",
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
      // drug:[
      //   {        
      //   name: "999感冒灵颗粒",
      //   dose: 3,
      //   frequency: "一日3次"
      //   },
      //   {        
      //   name: "盐酸左氧氟沙星片",
      //   dose: 3,
      //   frequency: "一日3次"
      //   },
      //   {        
      //   name: "健胃消食片",
      //   dose: 3,
      //   frequency: "一日3次"
      //   },
      // ],
      signature: '',
      advice: "要求自动转院，自愿承担转院风险，后果自负。",
      restaurants: [],
      caseInfo: [],
      tableData: [],
      infByHospital: [],
      infByDoctor: [],
      information: {},
      drug: [
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
      advice: "多喝水",
      transferReason: '',
      suggestions: [
        { value: "由于我院当前技术水平、设备条件，不能确诊或治疗条件有限的患者。" },
        { value: "患者病情稳定。" },
        { value: "患者及家属要求转诊转院者。" }
      ]
    }
  },
  created() {
    this.loadByUser()
    this.loadByHospital()
    //this.loadByDoctor()
  },
  mounted() {
      this.restaurants = this.loadTransferReason();
    },
  methods: {
    loadByUser() {
      this.$request.get('/record/selectAll', { // 只能再就诊记录中选择要转诊的患者
        params: {
          doctorId: this.user.id
        }
      }).then(res => {
        this.tableData = res.data
        console.log(this.tableData)
      })
    },
    loadByDoctor() {
      let id = this.transferInHospital
      this.$request.get('/doctor/selectByH/' + id).then(res => {
        if (res.code === '200') {
          this.infByDoctor = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadByHospital() {
      this.$request.get('/hospital/selectAll').then(res => {
        this.infByHospital = res.data
      })
    },
    /**
     * 转诊申请
     */
    confirmTransfer() {
      this.information.userId = this.caseInfo.userId
      this.information.outHospitalId = this.caseInfo.hospitalId
      this.information.outDoctorId = this.caseInfo.doctorId
      this.information.inHospitalId = this.transferInHospital
      // this.information.inDoctorId=this.transferInDoctor // 医生由管理员分配
      this.information.why = this.transferReason
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
    },

    //转院理由联想
    querySearch(queryString, cb) {
        var restaurants = this.restaurants;
        var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      loadTransferReason() {
        return [
          { "value": "由于我院当前技术水平、设备条件，不能确诊或治疗条件有限的患者。"},
          { "value": "患者病情稳定。" },
          { "value": "患者及家属要求转诊转院者。"},
        ];
      },
    },
    transferReasonComplete(queryString, cb) {
      let results = this.suggestions.filter(item => item.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
      cb(results);
    },
    handleSelect(item) {
      // 这里可以添加当选中某个联想数据后的具体处理逻辑，比如赋值给其他变量等
      this.transferReason = item.value;
    }
  }
}
</script>

<style scoped>
.form-container {
  display: flex;
  justify-content: space-between;
}

.left-form,
.right-form {
  flex: 1;
  margin-right: 20px;
}

.label {
  margin-bottom: 10px;
  font-weight: bold;
}

.el-input,
.el-date-picker {
  margin-bottom: 20px;
}

.el-textarea {
  margin-top: 20px;
}
</style>
