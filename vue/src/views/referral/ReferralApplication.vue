<template>
  <el-card>
    <h1 style="text-align: center; margin-bottom: 10px">医院转诊申请表</h1>
    <el-form
      ref="elForm"
      :model="formData"
      :rules="rules"
      size="medium"
      label-width="auto"
      label-position="right"
    >
      <el-row :gutter="24">
        <el-col
          :span="12"
          style="border: 1px solid #ccc; border-radius: 10px; padding: 20px"
        >
          <el-row :gutter="24">
            <el-col :span="hsl">
              <el-form-item label="患者姓名" prop="">
                <el-select
                  v-model="formData.userName"
                  placeholder="请选择患者"
                  @change="handlePatientSelect"
                >
                  <el-option
                    v-for="item in patients"
                    :key="item.userName"
                    :label="item.userName"
                    :value="item.userName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="hsr">
              <el-form-item class="custom-label" label="转诊类型" prop="">
                <el-select v-model="formData.referralType" placeholder="请选择转诊类型">
                  <el-option label="普通" value="普通"></el-option>
                  <el-option label="急诊" value="急诊"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="9">
              <el-form-item label="年龄">
                <el-input v-model="formData.age" placeholder=""></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item class="custom-label" label="性别" label-width="50px" prop="">
                <el-input v-model="formData.sex"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="身份证号码" label-width="90px">
                <el-input v-model="formData.idCard" placeholder=""></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="hsl">
              <el-form-item label="联系电话">
                <el-input v-model="formData.phone" placeholder=""></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="hsr">
              <el-form-item label="转诊编号">
                <el-input v-model="formData.referralNumber" placeholder=""></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="初步诊断" prop="diagnosis">
            <el-input
              v-model="formData.diagnosis"
              show-word-limit
              clearable
              prefix-icon="el-icon-first-aid-kit"
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="沟通记录" prop="communication">
            <el-input
              v-model="formData.communication"
              placeholder="沟通记录内容"
              clearable
              prefix-icon="el-icon-chat-dot-square"
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="患者申请原因" prop="reason">
            <el-autocomplete
              v-model="formData.reason"
              type="textarea"
              placeholder="请输入患者申请原因"
              :autosize="{ minRows: 2, maxRows: 4 }"
              :style="{ width: '100%' }"
              @select="handleSelect"
              :fetch-suggestions="transferReasonComplete"
            ></el-autocomplete>
          </el-form-item>
          <el-form-item label="患者签字" prop="signatrue">
            <!-- 用 canvas 实现签字 -->
            <canvas
              ref="signatureCanvas"
              height="100px"
              style="border: 1px solid #ccc; background-color: #f9f9f9"
              @mousedown="startDrawing"
              @mousemove="drawSignature"
              @mouseup="endDrawing"
              @mouseleave="endDrawing"
            ></canvas>
            <el-button @click="clearSignature">清空签字</el-button>
          </el-form-item>
        </el-col>

        <!-- LEFT SIDE -->
        <el-col :span="s_half">
          <div style="border: 1px solid #ccc; border-radius: 10px; padding: 20px">
            <el-row :gutter="g_nor">
              <el-col :span="10">
                <el-form-item label="转诊状态">
                  <el-input v-model="formData.referralStatus"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="14">
                <el-form-item label="病历地址">
                  <el-input v-model="formData.traverseAddr" placeholder=""></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="g_nor">
              <el-col :span="13">
                <el-form-item label="转出医院" prop="outHospital">
                  <el-select
                    v-model="formData.outHospitalName"
                    placeholder="请选择转出医院"
                    clearable
                    :style="{ width: '100%' }"
                  >
                    <template #prefix>
                      <i class="el-icon-office-building"></i>
                    </template>
                    <el-option
                      v-for="item in hospitalOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="转出医生" prop="outDoctor">
                  <el-input v-model="formData.outDoctorName" prefix-icon="el-icon-user" readonly></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="转出日期" prop="outTime">
              <el-input
                v-model="formData.outTime"
                placeholder="请输入转出日期"
                clearable
                prefix-icon="el-icon-date"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
            <el-form-item label="医务科意见" prop="outHospitalAdvice">
              <el-input
                v-model="formData.outHospitalAdvice"
                placeholder="请输入医务科意见"
                clearable
                prefix-icon="el-icon-tickets"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </div>

          <br /><br />
          <div style="border: 1px solid #ccc; border-radius: 10px; padding: 20px">
            <el-row :gutter="24">
              <el-col :span="13">
                <el-form-item label="转入医院" prop="inHospital">
                  <el-select
                    v-model="formData.inHospitalName"
                    placeholder="请选择转入医院"
                    clearable
                    :style="{ width: '100%' }"
                  >
                    <template #prefix>
                      <i class="el-icon-office-building"></i>
                    </template>
                    <el-option
                      v-for="item in hospitalOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="转入医生" prop="inDoctor">
                  <el-input
                    v-model="formData.inDoctorName"
                    placeholder="请输入转入医生"
                    clearable
                    prefix-icon="el-icon-user"
                    :style="{ width: '100%' }"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="转入日期" prop="inTime">
              <el-input
                v-model="formData.inTime"
                placeholder="请输入转入日期"
                clearable
                prefix-icon="el-icon-date"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
            <el-form-item label="医务科意见" prop="inHospitalAdvice">
              <el-input
                v-model="formData.inHospitalAdvice"
                placeholder="请输入医务科意见"
                clearable
                prefix-icon="el-icon-tickets"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
            <el-form-item label="医保经办机构意见" prop="globalAdvice">
              <el-input
                v-model="formData.globalAdvice"
                placeholder="请输入医保经办机构意见"
                clearable
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
    </el-form>
    <div class="button-group">
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </div>
  </el-card>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      signatureBlob: null,
      formData: {
        userName: undefined,
        diagnosis: "",
        communication: undefined,
        reason: undefined,
        signature: undefined,
        outHospitalName: undefined,
        outDoctorName: undefined,
        outTime: undefined,
        outHospitalAdvice: undefined,
        inHospitalName: undefined,
        inDoctorName: undefined,
        inTime: undefined,
        inHospitalAdvice: undefined,
        globalAdvice: undefined,
        referralType: undefined,
        referralNumber: undefined,
        referralStatus: "待审批",
        communication: "要求自动转院，自愿承担转院风险，后果自负。",
        traverseAddr: undefined,
        signatureUrl: undefined,
      },
      rules: {
        diagnosis: [
          {
            required: false,
            message: "诊断结果",
            trigger: "blur",
          },
        ],
        communication: [
          {
            required: true,
            message: "沟通记录内容",
            trigger: "blur",
          },
        ],
        reason: [
          {
            required: true,
            message: "请输入患者申请原因",
            trigger: "blur",
          },
        ],
        signature: [
          {
            required: true,
            message: "",
            trigger: "blur",
          },
        ],
        outHospital: [
          {
            required: true,
            message: "请输入转出医院",
            trigger: "blur",
          },
        ],
        outDoctor: [
          {
            required: true,
            message: "请输入转出医生",
            trigger: "blur",
          },
        ],
        outTime: [
          {
            required: true,
            message: "请输入转出日期",
            trigger: "blur",
          },
        ],
        outHospitalAdvice: [
          {
            required: true,
            message: "请输入医务科意见",
            trigger: "blur",
          },
        ],
        inHospital: [
          {
            required: true,
            message: "请输入转入医院",
            trigger: "blur",
          },
        ],
        inDoctor: [
          {
            required: true,
            message: "请输入转入医生",
            trigger: "blur",
          },
        ],
        inTime: [
          {
            required: true,
            message: "请输入转入日期",
            trigger: "blur",
          },
        ],
        inHospitalAdvice: [
          {
            required: true,
            message: "请输入医务科意见",
            trigger: "blur",
          },
        ],
        globalAdvice: [
          {
            required: true,
            message: "请输入医保经办机构意见",
            trigger: "blur",
          },
        ],
      },
      suggestions: [
        { value: "由于我院当前技术水平、设备条件，不能确诊或治疗条件有限的患者。" },
        { value: "患者病情稳定。" },
        { value: "患者及家属要求转诊转院者。" },
      ],
      patients: [],
      temp: undefined,
      g_nor: 24,
      s_half: 12,
      hsl: 14,
      hsr: 10,
      hospitalOptions: [],
    };
  },
  computed: {},
  watch: {
    "formData.userName": {
      handler(newVal) {
        if (newVal) {
          this.handlePatientSelect(newVal);
        }
      },
    },
  },
  created() {
    this.loadUsers();
    this.formData.outDoctorName = this.user.name;
    this.formData.outTime = new Date().toISOString().split('T')[0];
    this.loadHospitals();
  },
  mounted() {
    // 获取 canvas 的上下文
    this.canvasContext = this.$refs.signatureCanvas.getContext("2d");
    this.canvasContext.lineWidth = 2;
    this.canvasContext.lineCap = "round";
    this.canvasContext.strokeStyle = "#000"; // 签字颜色

    this.resizeCanvas();
    // 监听窗口大小变化事件，当窗口大小改变时重新调整 canvas 大小
    window.addEventListener("resize", this.resizeCanvas);
  },
  beforeDestroy() {
    // 移除窗口大小变化事件监听，避免内存泄漏
    window.removeEventListener("resize", this.resizeCanvas);
  },
  methods: {
    loadUsers() {
      this.$request
        .get("/record/doctor", {
          params: {
            doctorId: this.user.id,
            inHospital: "已住院",
          },
        })
        .then((res) => {
          if (res.code === "200") {
            // 解包Traverse字段
            console.log(res.data);
            this.patients = res.data.map((item) => ({
              ...item,
              ...item.traverse
            }));
            console.log(this.patients);
          }
        });
    },
    loadHospitals() {
      this.$request.get("/hospital/selectAll").then((res) => {
        if (res.code === "200") {
          this.hospitalOptions = res.data.map((item) => ({
            value: item.hospitalName,
            label: item.hospitalName,
          }));
        }
      });
    },
    handlePatientSelect(userName) {
      const selectedPatient = this.patients.find((p) => p.userName === userName);
      if (selectedPatient) {
        // 自动填充患者信息
        this.formData.sex = selectedPatient.sex;
        this.formData.age = selectedPatient.age;
        this.formData.idCard = selectedPatient.idCard;
        this.formData.phone = selectedPatient.phone;
        this.formData.diagnosis = selectedPatient.mainDiagnosis;
      }
    },
    handleSelect(item) {
      // 这里可以添加当选中某个联想数据后的具体处理逻辑，比如赋值给其他变量等
      this.formData.reason = item.value;
    },
    submitForm() {
      // 如果有签名，先上传签名图片
      if (this.signatureBlob) {
        // 使用原生 XMLHttpRequest 上传文件
        this.uploadSignatureWithXHR();
      } else {
        // 没有签名直接提交表单
        this.submitFullForm();
      }
    },
    
    // 使用原生 XMLHttpRequest 上传文件
    uploadSignatureWithXHR() {
      const formData = new FormData();
      formData.append('file', this.signatureBlob, 'signature.png');
      
      const xhr = new XMLHttpRequest();
      xhr.open('POST', this.$request.defaults.baseURL + '/files/upload', true);
      
      // 设置请求完成的回调函数
      xhr.onload = () => {
        if (xhr.status === 200) {
          try {
            const response = JSON.parse(xhr.responseText);
            if (response.code === '200') {
              // 将签名URL保存到表单数据中
              this.formData.signatureUrl = response.data;
              // 提交完整表单
              this.submitFullForm();
            } else {
              this.$message.error('签名上传失败: ' + response.msg);
            }
          } catch (e) {
            console.error('解析响应失败:', e);
            this.$message.error('签名上传失败: 无法解析响应');
          }
        } else {
          this.$message.error('签名上传失败: ' + xhr.statusText);
        }
      };
      
      // 设置上传进度回调
      xhr.upload.onprogress = (event) => {
        if (event.lengthComputable) {
          const percentComplete = (event.loaded / event.total) * 100;
          console.log('上传进度: ' + percentComplete + '%');
        }
      };
      
      // 设置请求错误的回调函数
      xhr.onerror = () => {
        console.error('上传请求失败');
        this.$message.error('签名上传请求失败');
      };
      
      // 发送请求
      xhr.send(formData);
    },
    
    // 提交完整表单的方法
    submitFullForm() {
      this.$request.post("/referral/add", this.formData).then((res) => {
        if (res.code === "200") {
          this.$message.success("提交成功");
          
          // 如果是急诊，继续处理区块链数据
          if (this.formData.referralType === "急诊") {
            const referralInfo = {}
            referralInfo.patientData = `${this.formData.userName}||${this.formData.sex}||${this.formData.age}||${this.formData.idCard}||${this.formData.phone}`
            referralInfo.medicalData = `${this.formData.diagnosis}||${this.formData.reason}||${this.formData.communication}||${this.formData.signatureUrl}`
            referralInfo.outHospitalData = `${this.formData.outHospitalName}||${this.formData.outDoctorName}||${this.formData.outHospitalAdvice}||${this.formData.outTime}`
            referralInfo.status = "待接收"
            referralInfo.urgency = "急诊"
            
            this.$blockRequest.post("/storeIntelReferralInfo", referralInfo).then((blockRes) => {
              if (blockRes.data.code === "200") {
                this.$message.success("区块链数据上传成功");
                this.formData.traverseAddr = blockRes.data.data.transactionReceipt.transactionHash
              } else {
                this.$message.error("区块链数据上传失败: " + (blockRes.data.msg || "未知错误"));
              }
            }).catch(error => {
              console.error("区块链请求错误:", error);
              this.$message.error("区块链数据上传失败: " + (error.message || "网络错误"));
            });
          }
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    resetForm() {
      this.$refs["elForm"].resetFields();
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
        // 将签名转为 Base64 数据
        const signatureData = this.$refs.signatureCanvas.toDataURL('image/png');
        // 将 Base64 转换为 Blob
        const byteString = atob(signatureData.split(',')[1]);
        const mimeString = signatureData.split(',')[0].split(':')[1].split(';')[0];
        const ab = new ArrayBuffer(byteString.length);
        const ia = new Uint8Array(ab);
        for (let i = 0; i < byteString.length; i++) {
          ia[i] = byteString.charCodeAt(i);
        }
        const blob = new Blob([ab], { type: mimeString });
        
        this.signatureBlob = blob;
      }
    },
    resizeCanvas() {
      this.$nextTick(() => {
        // 获取父元素的宽度，不包含margin
        const canvas = this.$refs.signatureCanvas;
        const parent = canvas.parentNode;
        const width = parent.clientWidth;
        canvas.width = width;
      });
    },
    clearSignature() {
      const canvas = this.$refs.signatureCanvas;
      const ctx = canvas.getContext("2d");
      ctx.clearRect(0, 0, canvas.width, canvas.height);
    },
    //转院理由联想
    transferReasonComplete(queryString, cb) {
      if (!queryString) return cb([]);
      let results = this.suggestions.filter(
        (item) => item.value.toLowerCase().includes(queryString.toLowerCase())
      );
      cb(results);
    },
  },
};
</script>
<style scoped>
.button-group {
  display: flex;
  justify-content: center;
}
.button-group .el-button {
  margin: 10px;
}

</style>
