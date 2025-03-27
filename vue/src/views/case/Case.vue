<template>
  <el-card class="case-container">
    <el-row class="info-row" :gutter="24">
      <!-- LEFT SIDE -->
      <el-col :span="18" >
        <h2 class="title">住院病历/门诊病历</h2>
        <div class="right-container">
          <!-- HEAD -->
          <el-row class="info-row" :gutter="24">
            <el-col :span="6">
              <span class="field-label2">姓名:</span>
              <span class="field-value2">{{ caseInfo.userName }}</span>
            </el-col>
            <el-col :span="6">
              <span class="field-label2">性别:</span>
              <span class="field-value2">{{ caseInfo.sex }}</span>
            </el-col>
            <el-col :span="6">
              <span class="field-label2">年龄:</span>
              <span class="field-value2">{{ caseInfo.age }}</span>
            </el-col>
            <el-col :span="6">
              <span class="field-label2">职业:</span>
              <span class="field-value2">{{ caseInfo.occupation }}</span>
            </el-col>

            <el-col :span="8">
              <span class="field-label2">入院/就诊时间:</span>
              <span class="field-value2">{{ recordDate }}</span>
            </el-col>
            <el-col :span="8">
              <span class="field-label2">记录时间:</span>
              <span class="field-value2">{{ recordDate }}</span>
            </el-col>

            <el-col :span="8">
              <span class="field-label2">联系方式:</span>
              <span class="field-value2">{{ caseInfo.phone }}</span>
            </el-col>
          </el-row>

        

        <!-- CASE DESCRIPTION -->
        <el-form>
          <div class="field-label">主诉（Chief Complaint, CC）</div>
          <el-form-item>
            <el-input
              type="textarea"
              v-model="caseInfo.illnessDetail"
              clearable
              :rows="2"
              resize="vertical"
              class="info-textarea CC"
              placeholder="患者就诊的主要症状或体征 + 持续时间。"
            ></el-input>
          </el-form-item>

          <div class="field-label">初步诊断（Primary Diagnosis）</div>
          <el-form-item label="1.主要诊断：">
            <el-input
              type="textarea"
              v-model="mainDiagnosis"
              clearable
              :rows="1"
              resize="vertical"
              class="info-textarea"
              placeholder="根据病史、查体及检查结果。"
            ></el-input>
          </el-form-item>
          <el-form-item label="2.次要诊断：">
            <el-input
              type="textarea"
              v-model="secondaryDiagnosis"
              clearable
              :rows="1"
              resize="vertical"
              class="info-textarea"
              placeholder="合并症或其他疾病。"
            ></el-input>
          </el-form-item>

          <div class="field-label">诊疗计划（Treatment Plan）</div>

          <el-select
            class="plan-select"
            v-model="selected_plan"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="选择诊疗计划"
          >
            <el-option
              v-for="item in plans"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>

          <el-form-item label="进一步检查:" v-if="selected_plan.includes('furtherCheck')">
            <el-input
              type="textarea"
              v-model="furtherCheck"
              clearable
              :rows="1"
              resize="vertical"
              class="info-textarea"
              placeholder="明确需完善的实验室或影像学检查。"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="药物治疗：" v-if="selected_plan.includes('medicine')">
            <el-input
              v-model="medicine"
              class="custom-input"
              placeholder="药物名称、剂量、用法、疗程。"
            >
              <el-button
                slot="append"
                icon="el-icon-plus"
                @click="showSelectMedicineDialog = true"
                type="primary"
              ></el-button>
            </el-input>
          </el-form-item>
          <el-form-item
            label="非药物治疗："
            v-if="selected_plan.includes('non-medicine')"
          >
            <el-input
              type="textarea"
              v-model="nonMedicine"
              clearable
              :rows="1"
              resize="vertical"
              class="info-textarea"
              placeholder="手术、康复训练、生活方式干预等。"
            ></el-input>
          </el-form-item>
          <el-form-item label="护理/监测:" v-if="selected_plan.includes('care')">
            <el-input
              type="textarea"
              v-model="care"
              clearable
              :rows="1"
              resize="vertical"
              class="info-textarea"
              placeholder="如监测生命体征、记录出入量等。"
            ></el-input>
          </el-form-item>
          <el-form-item label="饮食建议：" v-if="selected_plan.includes('diet')">
            <el-input
              type="textarea"
              v-model="diet"
              clearable
              :rows="1"
              resize="vertical"
              class="info-textarea"
              placeholder="如低盐、流质饮食等。"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      </el-col>

      <!-- RIGHT SIDE -->
      <el-col :span="6" style="margin-top: 15px;">
        <div>
          <!-- 这里放图 -->
          <span class="field-label">辅助检查（Auxiliary Examination）</span>
          <div class="upload-container">
            <el-upload
              ref="pictureUpload"
              :action="$baseUrl + '/files/upload'"
              list-type="picture-card"
              :auto-upload="true"
              :on-success="handleImgSuccess"
              :on-remove="handleRemove"
              :data="extraData"
              :before-upload="beforeUpload"
              class="custom-upload"
              multiple
              :limit="3"
            >
              <i slot="default" class="el-icon-plus"></i>
              <div slot="file" slot-scope="{ file }">
                <div class="uploaded-images">
                  <img class="el-upload-list__item-thumbnail" :src="file.url" />
                  <span class="el-upload-list__item-actions">
                    <span
                      class="el-upload-list__item-preview"
                      @click="handlePictureCardPreview(file)"
                    >
                      <i class="el-icon-zoom-in"></i>
                    </span>
                    <span
                      v-if="!disabled"
                      class="el-upload-list__item-delete"
                      @click="handleRemove(file, uploadedUrls)"
                    >
                      <i class="el-icon-delete"></i>
                    </span>
                  </span>
                </div>
              </div>
            </el-upload>
          </div>
        </div>

        <div class="confirm-button">
          <el-button type="primary" @click="ok">医生签名</el-button>
        </div>
      </el-col>
    </el-row>

    <!-- SELECT MEDICINE DIALOG -->
    <el-dialog
      :visible="showSelectMedicineDialog"
      title="选择药品"
      width="80%"
      @close="showSelectMedicineDialog = false"
      center
    >
      <SelectMedicine
        :drugList="drugList"
        @updateDrug="updateSelectMedicine"
        @close="showSelectMedicineDialog = false"
      ></SelectMedicine>
    </el-dialog>

    <!-- IMAGE PREVIEW DIALOG-->
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" />
    </el-dialog>

  </el-card>
</template>

<script>
import SelectMedicine from "../component/SelectMedicine.vue";

export default {
  name: "Case",
  components: {
    SelectMedicine,
  },
  data() {
    return {
      caseInfo: {}, // 单个病历信息
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      /** 主要诊断 */
      mainDiagnosis: "", 
      /** 次要诊断 */
      secondaryDiagnosis: "", 
      selectedMedicine: [],
      tableData: [],
      drugList: [],
      receivedData: {},
      restaurantsFrequency: [],
      restaurantsAdvice: [],
      restaurantsDiagnosis: [],
      dialogImageUrl: "",
      dialogVisible: false,
      disabled: false,
      uploadedUrls: [],
      uploadDisabled: false, // 控制上传组件的显示与隐藏
      showSelectMedicineDialog: false,
      extraData: { isTraverse: true },
      recordDate: "",
      imgURL: { img: "" },
      plans: [
        {
          value: "furtherCheck",
          label: "进一步检查",
        },
        {
          value: "medicine",
          label: "药物治疗",
        },
        {
          value: "non-medicine",
          label: "非药物治疗",
        },
        {
          value: "care",
          label: "护理/监测",
        },
        {
          value: "diet",
          label: "饮食建议",
        },
      ],
      selected_plan: ["furtherCheck", "medicine", "non-medicine", "care", "diet"],
      furtherCheck: null,
      medicine: null,
      nonMedicine: null,
      care: null,
      diet: null,
    };
  },
  created() {
    this.recordDate = new Date()
    this.recordDate = this.recordDate.toISOString().split("T")[0]
    this.loadData(); // 加载数据tableData和初始化caseInfo的通用方法
    this.loadByDrug(); //获取drugList
  },
  mounted() {
    this.restaurantsFrequency = this.loadFrequency();
    this.restaurantsAdvice = this.loadAdvice();
    this.restaurantsDiagnosis = this.loadDiagnosis();
  },
  methods: {
    async loadData() {
      try {
        // loadByUser返回Promises
        await Promise.all([this.loadByUser()]);
        // 从URL查询参数中解析caseInfo
        this.caseInfo = this.$route.query;
      } catch (error) {}
    },

    async loadByUser() {
      const res = await this.$request.get("/record/selectAll", {
        params: {
          doctorId: this.user.id,
        },
      });
      this.tableData = res.data;
    },

    async loadByDrug() {
      this.$request
        .get("/drug/selectAll", {
          params: {
            hospitalId: this.user.hospitalId,
          },
        })
        .then((res) => {
          res.data.map((item) => {
            item.label = item.drugName;
            item.key = item.id;
          });
          this.drugList = res.data;
        });
    },

    updateCaseInfoFromTableData() {
      // 更新caseInfo以匹配tableData中的项
      const matchingItem = this.tableData.find(
        (item) =>
          item.userName === this.caseInfo.userName || item.userName === this.caseInfo.name
      );
      if (matchingItem) {
        // 使用解构赋值或直接赋值来更新caseInfo
        // 注意：直接赋值整个对象可能会覆盖Vue的响应式跟踪，除非matchingItem本身是一个响应式对象
        this.caseInfo = { ...matchingItem }; // 使用解构赋值来保持响应性
      }
      // 如果没有找到匹配项，caseInfo将保持不变（从URL查询参数中解析的值）
    },

    /**
     * 从子组件SelectMedicine中传递的已选择药品，格式化后更新到medicine
     * @param selectedDrugs 子组件传来的已选择的药品
     */
    updateSelectMedicine(selectedDrugs) {
      this.selectedMedicine = selectedDrugs;
      let t = '';
      this.selectedMedicine.map((item) => {
        t = t +
          item.drugName +
          " " +
          item.quantity +
          " " +
          item.frequency +
          "\n";
      });
      this.medicine = t;
    },
    /** 
     * 对病历签名
     */
    sign() {

    },
    ok() { 
      let newTraverse = {};
      newTraverse.userId = this.caseInfo.userId;
      newTraverse.doctorId = this.user.id;
      newTraverse.hospitalId = this.caseInfo.hospitalId;
      newTraverse.timestamp = new Date().getTime();
      newTraverse.illnessDetail = this.caseInfo.illnessDetail;
      newTraverse.treatmentDate = this.recordDate;
      newTraverse.recordDate = this.recordDate;
      newTraverse.inHospital = this.medicine === "无" ? "是" : "否";
      newTraverse.mainDiagnosis = this.mainDiagnosis
      newTraverse.secondaryDiagnosis = this.secondaryDiagnosis
      newTraverse.furtherCheck = this.furtherCheck
      newTraverse.drug = this.medicine;
      newTraverse.nonMedicine = this.nonMedicine
      newTraverse.care = this.care
      newTraverse.diet = this.diet
      newTraverse.img = this.imgURL.img;

      console.log("newTraverse", newTraverse)

      // 确认病历，上传到数据库
      this.$request.post("/traverse/add", newTraverse)
      .then((res) => {
        if (res.code === "200") {
          return this.$request.get("/traverse/selectAll", {
            params: {
              id: res.data,
            },
          });
        } else {
          this.$message.error("病历上传失败");
          return Promise.reject(res.msg);
        }
      })
      .then((res2) => {
        if (res2 && res2.code === "200") {
          this.$router.push({ name: 'CaseSign'});
          // 解包 traverse 对象并合并到顶层
          const traverseDTO = {
            ...res2.data[0], // 展开 TraverseDTO 的其他字段
            ...res2.data[0].traverse // 展开 traverse 对象的字段
          };
          delete traverseDTO.traverse;
          console.log("获取到的病历", traverseDTO)
          this.$store.commit('setTraverseData', traverseDTO);
        } else {
          this.$message.error("获取病历数据失败");
        }
      })
      .catch((error) => {
        this.$message.error("请求失败：" + (error.message || error));
      });
    },
    //每日次数输入框
    querySearch(queryString, cb) {
      var restaurantsFrequency = this.restaurantsFrequency;
      var results = queryString
        ? restaurantsFrequency.filter(this.createFilter(queryString))
        : restaurantsFrequency;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurantsFrequency) => {
        return (
          restaurantsFrequency.value.toLowerCase().indexOf(queryString.toLowerCase()) ===
          0
        );
      };
    },
    loadFrequency() {
      return [{ value: "一日一次" }, { value: "一日两次" }, { value: "一日三次" }];
    },
    handleSelectFrequency(item) {},

    //病情联想
    querySearchAdvice(queryString, cb) {
      var restaurantsAdvice = this.restaurantsAdvice;
      var results = queryString
        ? restaurantsAdvice.filter(this.createFilter(queryString))
        : restaurantsAdvice;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    loadAdvice() {
      return [
        { value: "住院开药" },
        { value: "发烧，四肢无力" },
        { value: "咳嗽，鼻塞，咽喉红肿" },
        { value: "腹泻" },
        { value: "皮肤红肿" },
      ];
    },

    //诊断结果联想
    querySearchDiagnosis(queryString, cb) {
      var restaurantsDiagnosis = this.restaurantsDiagnosis;
      var results = queryString
        ? restaurantsDiagnosis.filter(this.createFilter(queryString))
        : restaurantsDiagnosis;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    loadDiagnosis() {
      return [
        { value: "肺炎" },
        { value: "发烧" },
        { value: "流感" },
        { value: "肠胃炎" },
        { value: "皮肤过敏" },
      ];
    },

    //图片删除
    handleRemove(file, uploadedUrls) {
      let uploadFiles = this.$refs.pictureUpload.uploadFiles;
      for (var i = 0; i < uploadFiles.length; i++) {
        if (uploadFiles[i]["url"] == file.url) {
          //服务器端删除图片
          let fileIdentifier = uploadedUrls[i];
          this.$request
            .delete(`/files/${fileIdentifier}`)
            .then((response) => {
              uploadedUrls.splice(i, 1);
            })
            .catch((error) => {});
          //客户端删除图片
          uploadFiles.splice(i, 1);
          break;
        }
      }
      this.uploadDisabled = false; //显示上传组件
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleImgSuccess(response) {
      // 设置uploadDisabled为true，隐藏上传组件
      this.uploadDisabled = true;
      const uploadedUrlString = response.data; //从上传成功后返回的Url中获取图片在服务器中的名称，并将其存在数组里
      console.log("url",uploadedUrlString)
      const uploadedUrl = uploadedUrlString.split("/");
      const lastPart = uploadedUrl[uploadedUrl.length - 1];
      this.uploadedUrls.push(lastPart);
      //存入数据库的url
      if (this.imgURL.img === "") {
        this.imgURL.img = response.data + "\n";
      } else {
        this.imgURL = this.imgURL + response.data + "\n";
      }
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith("image/");

      if (!isImage) {
        this.$message.error("上传文件只能是图片格式!");
      }
      return isImage;
    },
  },
};
</script>

<style scoped>
.title {
  text-align: center;
}
.case-container {
  min-height: 100%;
}

::v-deep .el-card__body {
  padding-top: 0px;
}

::v-deep .el-row {
  margin: 0px;
}

.custom-col {
  margin-left: 0px;
  /* 减小左侧间距 */
  margin-right: 0px;
  /* 减小右侧间距 */
}

.case-header {
  margin-bottom: 20px;
  text-align: center;
}

.info-row {
  margin-top: 15px;
  margin-bottom: 30px;
}

.info-field {
  margin-bottom: 15px;
}

.field-label {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  margin-right: 10px;
  font-weight: bold;
}

.field-label2 {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  margin-right: 0px;
  width: 80px;
  font-weight: bold;
}

.field-value {
  margin-left: 10px;
  margin-right: 150px;
}

.field-value2 {
  margin-left: 10px;
  margin-right: 0px;
}

.info-textarea {
  width: 100%;
}

::v-deep .el-textarea__inner {
  /* border: none;
  border-bottom: 1px solid #dcdfe6;
  border-radius: 0;
  outline: none;
  box-shadow: none !important; */
  background-color: transparent;
  resize: none !important;
}

::v-deep .el-textarea__inner::-webkit-scrollbar {
  display: none;
}

.medicine-select {
  width: 50%;
  margin-right: 40px;
}

.quantity-input {
  width: 50%;
  margin-right: 10px;
}

.frequency-input {
  width: 50%;
}

.medicine-textarea {
  margin-top: 10px;
  margin-bottom: 10px;
  width: calc(100% - 10px);
}

.confirm-button {
  display: flex;
  justify-content: center;
  margin: 10px;
}

.edit-button {
  float: right;
  margin-top: 10px;
  margin-right: 10px;
  font-size: 16px;
}

.divider {
  border: 1px solid #dcdcdc;
  margin-bottom: 20px;
}

.el-form-item {
  margin-bottom: 3px;
  display: flex;
}

::v-deep .el-form-item .el-form-item__content {
  flex: 1;
}

::v-deep .el-form-item__label {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  font-weight: bold;
}

.custom-form-inline .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
}

.el-upload--picture-card {
  width: 50px;
  height: 50px;
}

::v-deep .el-upload-list--picture-card .el-upload-list__item {
  transform-origin: top left;
  width: 100%;
  height: 100%;
}

::v-deep .el-upload-list__item-thumbnail {
  object-fit: contain;
}

::v-deep .disabled .el-upload--picture-card {
  display: none !important;
}

.plan-select {
  width: 100%;
  margin: 3px 0;
}
.right-container {
  margin-top: 10px;
  border: 1px solid;
  border-color: #c0c4cc; 
  border-radius: 5px;
  padding: 10px;
}
</style>
