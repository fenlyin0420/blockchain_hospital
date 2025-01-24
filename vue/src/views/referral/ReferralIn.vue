<template>
  <div>
    <div class="search">
      <div class="search1">
        <label for="search">搜索:</label>
        <el-input
          id="search"
          placeholder="请输入关键词"
          style="width: 200px"
          v-model="keywords"
        ></el-input>
        <el-button type="info" plain @click="load(1)">查询</el-button>
        <el-button type="warning" plain @click="reset">重置</el-button>
      </div>

      <div class="update">
        <label for="pull">转诊信息:</label>
        <el-input
          id="pull"
          placeholder="输入转诊信息"
          style="width: 200px"
          v-model="referralInfo"
        ></el-input>
        <el-button type="primary" plain @click="pullReferralInfo">获取</el-button>
      </div>
      
      <div class="accept">
        <el-button type="success" plain @click="accept">同意转入</el-button>
      </div>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="userName" label="姓名"  width="100" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="treatmentDate" label="就诊日期" width="200" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="hospitalName" label="医院名称"  width="100" align="center"></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名"  width="100" align="center"></el-table-column>
        <el-table-column prop="diagnosis" label="诊断结果" align="center"></el-table-column>
        <el-table-column label="详情"  align="center">
          <template v-slot="scope">
            <el-button plain type="" size="mini" @click="goToCaseDetails(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog
      :visible="showDialog"
      top="calc(100% / 4)"
      title="溯源病历"
      center
      fullscreen
      @close="handleClose"
    >
      <el-table :data="ReferralRecord" stripe>
        <el-table-column prop="userName" label="患者姓名"></el-table-column>
        <el-table-column
          prop="outHospitalName"
          label="转出医院"
          width="80px"
        ></el-table-column>
        <el-table-column prop="outTime" label="转出时间"></el-table-column>
        <el-table-column prop="reason" label="转诊原因"></el-table-column>
        <el-table-column
          prop="inHospitalName"
          label="转入医院"
          show-overflow-tooltip
        ></el-table-column>
      </el-table>

      <el-radio v-model="opt" label="溯源指定病历" @change="handleRadioChange"></el-radio>
      <el-radio v-model="opt" label="溯源所有病历" @change="handleRadioChange"></el-radio>
      <el-input :placeholder="placeholder" v-model="idCard"></el-input>
      <el-button type="primary" plain @click="pullTraverse">溯源病历</el-button>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ReferralRecord",
  data() {
    return {
      tableData: [], // 所有的数据
      pageNum: 1, // 当前的页码
      pageSize: 10, // 每页显示的个数
      total: 0,
      keywords: '',
      referralInfo: '',
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      showDialog: false,
      showProgress: false,
      showVerifySign: false,
      progressPercentage: 0,
      recievedData: "",
      signData: "1",
      signKey: "2",
      signResult: "3",
      dialogTitle: "",
      pubs: [],
      blockInfo: [{ QR: "", blockHash: "NULL" }],
      opt: "溯源指定病历",
      placeholder: "请输入转诊hash",
      ReferralRecord: [],
      idCard: "",
    };
  },
  created() {
    // this.load(1)
    const storedData = localStorage.getItem('tableData');
    if (storedData) {
        this.tableData = JSON.parse(storedData);
    }
    const storedData2 = localStorage.getItem('ReferralRecord');
    if (storedData2) {
        this.ReferralRecord = JSON.parse(storedData2);
    }
  },
  methods: {
    /**
     * 同意转出
     * @param row 转诊记录
     */
    update(row) {
      let form = {
        id: row.id,
      };
      this.$request.put("/referral/agreenIn", form).then((res) => {
        if (res.code === "200") {
          // 表示成功保存
          this.load(1);
          this.record(row);
        } else {
          this.$message.error(res.msg); // 弹出错误的信息
        }
      });
    },
    refuse(row) {
      let form = {
        id: row.id,
      };
      this.$request.put("/referral/refuseIn", form).then((res) => {
        if (res.code === "200") {
          // 表示成功保存
          this.$message.success("拒绝接收");
          this.load(1);
          // this.record(row)
        } else {
          this.$message.error(res.msg); // 弹出错误的信息
        }
      });
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request
        .get("/traverse/selectPageReferralTraverse", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.tableData = res.data?.list;
          this.total = res.data?.total;
        });
    },
    reset() {
      this.status = null;
      this.load(1);
    },
    /**
     * 获取病历
     */
    pullTraverse() {
      this.$message(this.opt)
      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });

      if (this.opt === "溯源指定病历") {
        ;
      } else {
        Request.post("/getMedicalRecordsByIdCard", {_idCard: this.idCard}).then((res) => {
          if (res.data.code === "200"){
            // 解析数据
            this.tableData = this.parseTraverse(res.data.data.returnObject)
            localStorage.setItem('tableData', JSON.stringify(this.tableData));
          }
        });
      }
    },
    /**
     * 根据给定地址，从区块链获取对应病历
     */
    pullReferralInfo() {
      // this.showDialog = true;
      // this.showProgress = true;
      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });

      // 1. 根据转诊hash获取转诊信息,并存储转诊信息
      Request.post("/getReferralInfoByTransactionHash", {
        _transactionHash: this.referralInfo,
      }).then((res) => {
        if (res.data.code === "200"){
          // 解析数据，打开浮窗，准备拉取病历
          this.ReferralRecord[0] = this.parseReferralRecord(res.data.data.returnObject)
          localStorage.setItem('ReferralRecord', JSON.stringify(this.ReferralRecord));
          this.showDialog = true
        }
      });
    },
    /**
     * 同意转诊，存储转诊信息
     */
    accept(){
      let body = this.ReferralRecord[0]
      body.result = "已转入" // 设置转诊结果
      body.inTime = new Date().toISOString().split('T')[0]
      this.$request.post("/referral/add", body).then((res) => {
        if(res.code === '200') {
          localStorage.removeItem('ReferralRecord');
          localStorage.removeItem('tableData');
          this.ReferralRecord = []
          this.tableData = []
          this.$message.success("转入成功");
        }
      });
    },
    async goToCaseDetails(row) {
      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });
      // 获取环公钥
      await Request.post("/getPublicKeyByTransactionHash", {
        "_transactionHash": "0x5aafa7cd8ef6e7dc20c4740180a040fbcbee8666a3146a1ebd7f6eec393486ad"
      }).then((res) => {
        if (res.data.code === '200') {
          row.signPubKey = res.data.data.returnObject[0]
        } else {
          this.$message.error("获取环公钥失败")
        }
      });
      // 获取签名数据
      await this.$request.post("/traverse/getSignData", row).then((res) => {
        if (res.code === '200'){
          row.signData = res.data
        } else {
          this.$message.error("获取签名数据失败")
        }
      });

      row.signResult = ""
      console.log(row)
      this.$router.push({
        name: 'CaseDetail',
        query: row
      });
    },

    startProgress(traverse) {
      var index = 0;
      const keys = Object.keys(traverse);
      this.progressPercentage = 0;
      let interval = setInterval(() => {
        if (this.progressPercentage >= 100) {
          clearInterval(interval);
        } else {
          this.progressPercentage += 10;
        }
        if (index <= keys.length) {
          this.sendData = keys[index] + " : " + traverse[keys[index]];
          index++;
        }
      }, 1000);

      // 模拟操作完成后，可以取消进度条显示, 并显示验签页面
      setTimeout(() => {
        clearInterval(interval);
        this.showProgress = false;
        this.showVerifySign = true;
        this.dialogTitle = "环签名验证";
        this.$message({
          message: "发送成功",
          type: "success",
        });
      }, 1100); // 假设操作需要5秒
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    handleRadioChange(label){
      if (label === "溯源指定病历") 
        this.placeholder = "请输入转诊hash"
      else
        this.placeholder = "请输入身份证号"
    },
    handleClose() {
      this.showDialog = false;
    },
    parseReferralRecord(dataList ) {
      const parsedObject = {};
      // 去除多余换行符并按换行符分割字符串
      const lines = dataList[0].trim().split('\n');
      for (const line of lines) {
          // 按冒号分割键值对
          const [key, value] = line.split(': ');
          if (key && value) {
              parsedObject[key] = value;
          }
      }
      return this.convertToEnglishObject(parsedObject);
    },

    /**
     * 解析为病历列表 
     * @param dataList 病历列表
     */
    parseTraverse(dataList) {
      return dataList.flatMap(item => {
          return item.split('\n\n\n')
            .filter(entry => entry.trim()!== '')
            .map(entry => {
                  const lines = entry.split('\n').filter(line => line.trim()!== '');
                  const obj = {};
                  lines.forEach(line => {
                      const colonIndex = line.indexOf(':');
                      if (colonIndex!== -1) {
                          const key = line.slice(0, colonIndex).trim();
                          const value = line.slice(colonIndex + 1).trim();
                          obj[key] = value;
                      }
                  });
                  return this.convertToEnglishObject(obj);
              });
      });
    },
    convertToEnglishObject(chineseObj) {
      return Object.entries(chineseObj).reduce((acc, [key, value]) => {
        switch (key) {
          case "病例编号":
            acc.caseNumber = value;
            break;
          case "患者姓名":
            acc.userName = value;
            break;
          case "身份证号":
            acc.idCard = value;
            break;
          case "医生姓名":
            acc.doctorName = value;
            break;
          case "aa":
            acc.hospitalName = value;
            break;
          case "医嘱":
            acc.advice = value;
            break;
          case "诊断结果":
            acc.diagnosis = value;
            break;
          case "药物":
            acc.drug = value;
            break;
          case "是否住院":
            acc.inHospital = value;
            break;
          case "时间戳":
            acc.timestamp = value;
            break;
          case "签名结果":
            acc.signKey = value;
            break;
          case "治疗日期":
            acc.treatmentDate = value;
            break;
          case "药物医嘱-治理医嘱":
            acc.drug = value.split("-")[0];
            acc.advice = value.split("-")[1];
            break;
          case "医院-医生":
            acc.hospitalName = value.split("-")[0];
            acc.doctorName = value.split("-")[1];
            break;
          case "医疗影像":
            acc.img = value;
            break;
          // referralInfo convert
          case "转出医院":
            acc.outHospitalName = value;
            break;
          case "转入医院":
            acc.inHospitalName = value;
            break;
          case "转出时间":
            acc.outTime = value;
            break;
          case "转诊原因":
            acc.reason = value;
            break;
        }
        return acc;
      }, {});
    },
  },
};
</script>

<style scoped>
.search * {
  margin-right: 10px;
  display: inline-block;
}

.search label {
  margin-right: 5px;
}

.search > .update {
  margin-left: 100px;
}

.el-table {
  color: blue;
}

::v-deep .el-textarea__inner {
  color: blue;
}

.verifyBtn {
  margin-top: 5px;
  width: 30%;
  margin-left: 70%;
}
</style>
