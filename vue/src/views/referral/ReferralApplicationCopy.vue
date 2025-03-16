<template>
  <el-card class="container">
    <div class="form-container">
      <!-- 左侧表单 -->
      <div class="left-form">
        <div class="label">转诊患者</div>
        <el-select
          v-model="caseInfo.userName"
          placeholder="请选择患者"
          style="width: 80%; margin-bottom: 20px"
        >
          <div v-for="item in tableData">
            <el-option :label="item.userName" :value="item.userName"></el-option>
          </div>
        </el-select>

        <div class="label">转出时间</div>
        <el-input
          v-model="outTime"
          :readonly="true"
          clearable
          style="width: 80%"
        ></el-input>

        <div class="label">沟通记录表</div>
        <el-input
          type="textarea"
          placeholder="患者承诺"
          v-model="promise"
          style="width: 80%"
        ></el-input>

        <div class="label">转诊理由</div>
        <el-autocomplete
          type="textarea"
          v-model="transferReason"
          :fetch-suggestions="transferReasonComplete"
          clearable
          placeholder="请输入转诊理由"
          @select="handleSelect"
          style="width: 80%; display: block"
        />

        <el-button
          type="primary"
          style="margin-top: 10px; position: absolute; right: 45%"
          @click="confirmTransfer"
          >授权提交</el-button
        >
      </div>

      <!-- 右侧表单 -->
      <div class="right-form">
        <el-form label-width="0px" style="margin-top: 20px">
          <div class="label">转入信息</div>
          <el-select
            v-model="transferInHospital"
            placeholder="请选择医院"
            @change="loadByDoctor()"
            style="width: 90%; margin-bottom: 20px"
          >
            <div v-for="item in infByHospital">
              <el-option
                :label="item.hospitalName"
                :value="item.hospitalName"
              ></el-option>
            </div>
          </el-select>

          <div class="label">患者签字</div>
          <el-form-item label="患者签字">
            <!-- 用 canvas 实现签字 -->
            <canvas
              ref="signatureCanvas"
              width="400"
              height="200"
              style="border: 1px solid #ccc; background-color: #f9f9f9"
              @mousedown="startDrawing"
              @mousemove="drawSignature"
              @mouseup="endDrawing"
              @mouseleave="endDrawing"
            ></canvas>
            <el-button @click="clearSignature">清空签字</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>


  </el-card>
</template>

<script>
import axois from "axios";

export default{
  name: "ReferralApplication",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      name: "",
      transferOutHospital: "",
      transferOutDoctor: "",
      transferOutTime: null,
      transferInHospital: "",
      transferInDoctor: "",
      transferInTime: null,
      transferReason: "",
      restaurants: [],
      caseInfo: { userName: "" },
      outTime: "",
      tableData: [],
      infByHospital: [{ hospitalName: "xx大学第二附属医院" }],
      infByDoctor: [],
      information: {},
      promise: "要求自动转院，自愿承担转院风险，后果自负。",
      transferReason: "",
      signature: "",
      suggestions: [
        { value: "由于我院当前技术水平、设备条件，不能确诊或治疗条件有限的患者。" },
        { value: "患者病情稳定。" },
        { value: "患者及家属要求转诊转院者。" },
      ],
      signature: "",
      isDrawing: false,
      canvasContext: null,

    };
  },
  created() {
    this.loadUsers();
  },
  mounted() {
    // 获取 canvas 的上下文
    this.canvasContext = this.$refs.signatureCanvas.getContext("2d");
    this.canvasContext.lineWidth = 2;
    this.canvasContext.lineCap = "round";
    this.canvasContext.strokeStyle = "#000"; // 签字颜色
    let tempDate = new Date();
    this.outTime = tempDate.toISOString().split("T")[0];
  },
  methods: {
    loadUsers() {
      this.$request
        .get("/record/selectAllUserName")
        .then((res) => {
          this.tableData = res.data;
        });
    },

    loadByHospital() {
      this.$request.get("/hospital/selectAll").then((res) => {
        this.infByHospital = res.data;
      });
    },
    /**
     * 转诊申请
     */
    confirmTransfer() {
      this.information.userName = this.caseInfo.userName;
      this.information.outDoctorName = this.user.name
      this.information.outHospitalName = "xx大学第一附属医院";
      this.information.inHospitalName = this.transferInHospital;
      this.information.reason = this.transferReason;
      this.information.outTime = this.outTime;
      this.information.result = "待审批";
      console.log(this.information);
      let data = JSON.parse(JSON.stringify(this.information));
      this.$request.post("/referral/add", data).then((res) => {
        if (res.code === "200") {
          this.$message.success("申请成功");
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    //转院理由联想
    transferReasonComplete(queryString, cb) {
      let results = this.suggestions.filter(
        (item) => item.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1
      );
      cb(results);
    },
    handleSelect(item) {
      // 这里可以添加当选中某个联想数据后的具体处理逻辑，比如赋值给其他变量等
      this.transferReason = item.value;
    },
    startDrawing(event) {
      this.isDrawing = true;
      const { offsetX, offsetY } = event;
      this.canvasContext.moveTo(offsetX, offsetY);
      this.canvasContext.beginPath();
    },
    drawSignature(event) {
      if (!this.isDrawing) return;
      const { offsetX, offsetY } = event;
      this.canvasContext.lineTo(offsetX, offsetY);
      this.canvasContext.stroke();
    },
    endDrawing() {
      if (this.isDrawing) {
        this.isDrawing = false;
        this.signature = this.$refs.signatureCanvas.toDataURL(); // 将签名转为 Base64 数据
      }
    },
    clearSignature() {
      this.canvasContext.clearRect(
        0,
        0,
        this.$refs.signatureCanvas.width,
        this.$refs.signatureCanvas.height
      ); // 清除 canvas 内容
      this.signature = ""; // 清空输入框内容
    },

  },
};
</script>

<style scoped>
.container {
  padding: 20px;
  height: 95%;
  overflow-y: scroll;
}

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
  font-family: "SimSun", "宋体", serif;
  font-size: 16px;
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

::v-deep .el-textarea__inner {
  font-family: "SimSun", "宋体", serif;
  color: blue;
  font-size: 16px;
}

::v-deep .el-input__inner {
  font-family: "SimSun", "宋体", serif;
  color: blue;
  font-size: 16px;
}
</style>
