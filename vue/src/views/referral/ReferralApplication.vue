<template>
  <el-card>
    <h1 style="text-align: center; margin-bottom: 30px">医院转诊申请表</h1>
    <el-form
      ref="elForm"
      :model="formData"
      :rules="rules"
      size="medium"
      label-width="auto"
      label-position="right"
    >
      <el-row :gutter="24">
        <el-col :span="12">
          <el-row :gutter="24">
            <el-col :span="hsl">
              <el-form-item label="患者姓名" prop="">
                <el-select v-model="formData.userName" placeholder="请选择患者">
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
              <el-form-item class="custom-label" label="性别" prop="">
                <el-input v-model="formData.diagnosis"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="hsr">
              <el-form-item label="年龄">
                <el-input v-model="temp" placeholder=""></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="hsl">
              <el-form-item label="身份证号码">
                <el-input v-model="temp" placeholder=""></el-input>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row :gutter="24">
            <el-col :span="hsl">
              <el-form-item label="联系电话">
                <el-input v-model="temp" placeholder=""></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="hsr">
              <el-form-item label="转诊编号">
                <el-input v-model="temp" placeholder=""></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="初步诊断" prop="diagnosis">
            <el-input
              v-model="formData.diagnosis"
              :maxlength="11"
              show-word-limit
              readonly
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
            <el-input
              v-model="formData.reason"
              type="textarea"
              placeholder="请输入患者申请原因"
              :autosize="{ minRows: 2, maxRows: 4 }"
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="患者签字" prop="signatrue">
            <!-- 用 canvas 实现签字 -->
            <canvas
              ref="signatureCanvas"
              height="200"
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
          <el-row :gutter="g_nor">
            <el-col :span="15">
              <el-form-item label="病历地址">
                <el-input v-model="temp" placeholder=""></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="9">
              <el-form-item label="转诊状态">
                <el-input v-model="temp" placeholder=""></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="g_nor">
            <el-col :span="13">
              <el-form-item label="转出医院" prop="outHospital">
                <el-input
                  v-model="formData.outHospital"
                  placeholder="请输入转出医院"
                  clearable
                  prefix-icon="el-icon-office-building"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="转出医生" prop="outDoctor">
                <el-input
                  v-model="formData.outDoctor"
                  placeholder="请输入转出医生"
                  clearable
                  prefix-icon="el-icon-user"
                  :style="{ width: '100%' }"
                ></el-input>
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
          <br /><br />
          <el-row :gutter="24">
            <el-col :span="13">
              <el-form-item label="转入医院" prop="inHospital">
                <el-input
                  v-model="formData.inHospital"
                  placeholder="请输入转入医院"
                  clearable
                  prefix-icon="el-icon-office-building"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="转入医生" prop="inDoctor">
                <el-input
                  v-model="formData.inDoctor"
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

          <div style="display: flex; justify-content: space-around">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </el-card>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        userName: undefined,
        diagnosis: "",
        communication: undefined,
        reason: undefined,
        signature: undefined,
        outHospital: undefined,
        outDoctor: undefined,
        outTime: undefined,
        outHospitalAdvice: undefined,
        inHospital: undefined,
        inDoctor: undefined,
        inTime: undefined,
        inHospitalAdvice: undefined,
        globalAdvice: undefined,
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
      patients: [],
      temp: undefined,
      g_nor: 24,
      s_half: 12,
      hsl: 14,
      hsr: 10
    };
  },
  computed: {},
  watch: {},
  created() {
    this.loadUsers();
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
      this.$request.get("/record/selectAllUserName").then((res) => {
        this.patients = res.data;
      });
    },
    submitForm() {
      this.$refs["elForm"].validate((valid) => {
        if (!valid) return;
        // TODO 提交表单
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
        this.signature = this.$refs.signatureCanvas.toDataURL(); // 将签名转为 Base64 数据
      }
    },
    resizeCanvas() {
      this.$nextTick(() => {
        // 获取父元素的宽度，不包含margin
        const canvas = this.$refs.signatureCanvas;
        const parent = canvas.parentNode;
        const width = parent.clientWidth;
        canvas.width = width;
        canvas.height = 200;
      })
    },
    clearSignature() {
      const canvas = this.$refs.signatureCanvas;
      const ctx = canvas.getContext("2d");
      ctx.clearRect(0, 0, canvas.width, canvas.height);
    },
  },
};
</script>
<style scoped>
::v-deep.custom-label .el-form-item_label {
  width: 100px !important;
}
</style>
