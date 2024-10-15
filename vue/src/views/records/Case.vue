<template>
  <div class="case-container">
    <div class="case-header">
      <h2 style="margin: 0; text-align: left;">病历详情</h2>
    </div>

    <div class="case-info">
      <el-row class="info-row">
        <el-col :span="6">
          <span class="field-label">选择:</span>
          <el-select v-model="caseInfo" placeholder="请选择医院">
            <div v-for="item in tableData">
              <el-option :label="item.userName + '-' + item.hospitalName" :value="item"></el-option>
            </div>
          </el-select>
        </el-col>
        <el-col :span="6">
          <div class="info-field">
            <span class="field-label">姓名:</span>
            <span class="field-value">{{ caseInfo.userName }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-field">
            <span class="field-label">医生姓名:</span>
            <span class="field-value">{{ caseInfo.doctorName }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-field">
            <span class="field-label">医院名称:</span>
            <span class="field-value">{{ caseInfo.hospitalName }}</span>
          </div>
        </el-col>
        <el-row class="info-row">
          <el-col :span="6">
            <div class="info-field">
              <span class="field-label">科室:</span>
              <span class="field-value">{{ caseInfo.departmentName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-field">
              <span class="field-label">检查时间:</span>
              <span class="field-value">{{ caseInfo.time }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-field">
              <span class="field-label">住院 :</span>
              <span class="field-value">{{ caseInfo.inhospital }}</span>
            </div>
          </el-col>
        </el-row>
      </el-row>

      <hr class="divider" />
      <el-row class="info-row">
        <el-col :span="24">
          <div class="info-field">
            <span class="field-label">医嘱信息:</span>
            <el-input type="textarea" v-model="advice" clearable :rows="5" resize="vertical" class="info-textarea"></el-input>
          </div>
        </el-col>
      </el-row>

      <hr class="divider" />
      <el-row class="info-row">
        <el-col :span="24">
          <div class="info-field">
            <span class="field-label">药品信息:</span>
            <div>
              <el-select v-model="selectedMedicine" placeholder="请选择药品" class="medicine-select">
                <div v-for="item in drugList">
                  <el-option :label="item.drugName" :value="item.drugName"></el-option>
                </div>
              </el-select>
              <el-input v-model="medicineQuantity" placeholder="数量" class="quantity-input"></el-input>
              <el-input v-model="medicineFrequency" placeholder="一日几次" class="frequency-input"></el-input>
              <el-button type="primary" @click="confirmMedicine" class="confirm-button">确定</el-button>
            </div>
            <el-input type="textarea" v-model="medicine" clearable :rows="5" resize="vertical" class="medicine-textarea"></el-input>
            <span class="field-label"> 是否住院 : </span>
              <el-radio v-model="radio" label="是" @change="handleRadioChange()">是</el-radio>
              <el-radio v-model="radio" label="否" @change="handleRadioChange()">否</el-radio>
            <el-button type="primary" @click="ok" class="edit-button">确定</el-button>
          </div>
        </el-col>
      </el-row>
      
      

    </div>
  </div>
</template>

<script>
export default {
  name: "Case",
  data() {
    return {
      caseInfo: {inhospital: this.radio}, // 单个病历信息
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      advice: '', // 医嘱
      medicine: '', // 药品
      selectedMedicine: '', // 选择的药品
      medicineQuantity: '', // 药品数量
      medicineFrequency: '', // 一日几次
      radio: '',
      tableData: [],
      drugList: [],
      information: {},
      receivedData: {}
    }
  },
  created() {
    this.loadByUser();
    this.loadByDrug();
    const queryData = this.$route.query.data;
    if (queryData) {
      // 对查询参数中的数据进行解码和解析
      this.caseInfo = JSON.parse(decodeURIComponent(queryData));
    }
  },
  mounted() {

  },
  methods: {
    loadByUser() {
      this.$request.get('/record/selectAll', {
        params: {
          doctorId: this.user.id
        }
      }).then(res => {
        this.$message.success("成功")
        this.tableData = res.data
      })
    },
    loadByDrug() {
      this.$request.get("/drug/selectAll",{
        params:{
          hospitalId:this.user.hospitalId
        }
      }).then(res => {
        this.drugList = res.data
      })
    },
    loadCaseInfo() {
      this.caseInfo = {
        id: '001',
        number: '123456',
        name: '默认',
        doctorName: '李医生',
        hospitalName: 'XX医院',
        status: '未叫号', // Assuming default status
      }
    },
    call(caseInfo) {
      let reserveData = { ...caseInfo, status: '已叫号' };
      // Simulating API request to update case status
      // this.$request.put('/case/update', reserveData).then(res => {
      //   if (res.code === '200') {
      //     this.$message.success('叫号成功')
      //     this.loadCaseInfo()
      //     this.record(caseInfo)
      //   }
      // })
      this.$message.success('叫号成功')
      this.loadCaseInfo()
      this.record(caseInfo)
    },
    record(caseInfo) {
      let data = {
        userId: caseInfo.userId,
        doctorId: caseInfo.doctorId,
      }
      // this.$request.post('/record/add', data).then(res => {
      //   if (res.code === '200') {
      //     this.$message.success('数据同步成功')
      //   } else {
      //     this.$message.error(res.msg)
      //   }
      // })
      // Assuming successful response for simulation
      this.$message.success('数据同步成功')
    },
    del(id) {
      // Simulating confirmation dialog and API request to delete case
      this.$confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', { type: "warning" }).then(response => {
        // Simulating API request to delete case
        // this.$request.delete('/case/delete/' + id).then(res => {
        //   if (res.code === '200') {
        //     this.$message.success('操作成功')
        //     this.loadCaseInfo()
        //   } else {
        //     this.$message.error(res.msg)
        //   }
        // })
        // Assuming successful response for simulation
        this.$message.success('操作成功')
        this.loadCaseInfo()
      }).catch(() => { })
    },
    confirmMedicine() {
      // 确定按钮
      this.medicine = this.medicine + this.selectedMedicine + " " + this.medicineQuantity + " " + this.medicineFrequency + "\n"
    },
    ok() {
      // 修改按钮
      this.information.number = new Date().getTime()
      this.information.name = this.caseInfo.userName
      this.information.doctorId = this.caseInfo.doctorId
      this.information.hospitalId = this.caseInfo.hospitalId
      this.information.advice = this.advice
      this.information.drug = this.medicine
      this.information.inhospital = this.radio
      this.information.jurisdiction = "允许"
      this.information.signData=" "
      this.information.signResult=" "
      this.information.signPubKey=" "
      this.information.signKey=" "
      this.$request.post('/traverse/add', this.information).then(res => {
        if (res.code === '200') {
          this.$message.success('成功la')

          this.$router.push({
            name: 'CaseDetails',
            params: {
              id: 1,
              name: this.user.name,
              doctor: '',
              hospitalId: 1,
              advice: this.advice,
              drug: this.drugList,
              inhospital: 'n',
              jurisdiction: '允许',
              doctorName: this.caseInfo.doctorname,
              hospitalName: this.caseInfo.hispitalName,
              number: this.user.number,
              signData: null,
              signResult: "成功",
              signPubKey: this.user.publickey,
              signKey: this.user.privateKey
            }
          })
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleRadioChange() {
      this.radio = this.radio === '是'? '是' : '否';
    }
  }
}
</script>

<style scoped>
.case-container {
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.case-header {
  margin-bottom: 20px;
  text-align: center;
}

.info-row {
  margin-top: 30px;
  margin-bottom: 6px;
}

.info-field {
  margin-bottom: 10px;
}

.field-label {
  font-weight: bold;
}


.field-value {
  margin-left: 10px;
}

.info-textarea {
  margin-bottom: 26px;
}

.medicine-select {
  width: calc(33.33% - 10px);
  margin-right: 40px;
}

.quantity-input {
  width: calc(18% - 10px);
  margin-right: 40px;
}

.frequency-input {
  width: calc(33.33% - 10px);
}

.medicine-textarea {
  margin-top: 10px;
  width: calc(100% - 10px);
}

.confirm-button {
  float: right;
  margin-top: 1px;
  margin-right: 12px;
  font-size: 12px;
}

.edit-button {
  float: right;
  margin-top: 10px;
  margin-right: 10px;
  font-size: 12px;
}

.divider {
  border: 1px solid #dcdcdc;
  margin-bottom: 20px;
}
</style>
