<template>
  <el-card class="case-container">
    <el-row class="info-row" :gutter="24">
      <!-- LEFT SIDE -->
      <el-col :span="16">
        <!-- HEAD -->
        <el-row class="info-row" :gutter="24">
          <el-col :span="6">
            <span class="field-label2">姓名:</span>
            <span class="field-value2">{{ caseInfo.userName }}</span>
          </el-col>
          <el-col :span="6">
            <span class="field-label2">性别:</span>
            <span class="field-value2">{{ caseInfo.userName }}</span>
          </el-col>
          <el-col :span="6">
            <span class="field-label2">年龄:</span>
            <span class="field-value2">{{ caseInfo.userName }}</span>
          </el-col>
          <el-col :span="6">
            <span class="field-label2">职业:</span>
            <span class="field-value2">{{ caseInfo.userName }}</span>
          </el-col>

          <el-col :span="12">
            <span class="field-label2">入院/就诊时间:</span>
            <span class="field-value2">{{ caseInfo.time }}</span>
          </el-col>
          <el-col :span="12">
            <span class="field-label2">记录时间:</span>
            <span class="field-value2">{{ caseInfo.time }}</span>
          </el-col>

          <el-col :span="12">
            <span class="field-label2">联系方式:</span>
            <span class="field-value2">{{ caseInfo.time }}</span>
          </el-col>
          <el-col :span="12">
            <span class="field-label2">病情陈述者:</span>
            <span class="field-value2">{{ caseInfo.time }}</span>
          </el-col>
        </el-row>
        
        <!-- CASE DESCRIPTION -->
        <el-form>
          <div class="field-label">主诉（Chief Complaint, CC）</div>
          <el-form-item>
            <el-autocomplete type="textarea" v-model="advice" clearable :rows="2" resize="vertical"
              class="info-textarea CC" :fetch-suggestions="querySearchAdvice"
              placeholder="患者就诊的主要症状或体征 + 持续时间。"></el-autocomplete>
          </el-form-item>

          <div class="field-label">初步诊断（Primary Diagnosis）</div>
          <el-form-item label="1.主要诊断：">
            <el-autocomplete type="textarea" v-model="diagnosis" clearable :rows="1" resize="vertical"
              class="info-textarea" :fetch-suggestions="querySearchDiagnosis"
              placeholder="根据病史、查体及检查结果。"></el-autocomplete>
          </el-form-item>
          <el-form-item label="2.次要诊断：">
            <el-autocomplete type="textarea" v-model="diagnosis" clearable :rows="1" resize="vertical"
              class="info-textarea" :fetch-suggestions="querySearchDiagnosis"
              placeholder="合并症或其他疾病。"></el-autocomplete>
          </el-form-item>

          <div class="field-label">诊疗计划（Treatment Plan）</div>
          <el-form-item label="1.进一步检查:">
            <el-autocomplete type="textarea" v-model="diagnosis" clearable :rows="1" resize="vertical"
              class="info-textarea" :fetch-suggestions="querySearchDiagnosis"
              placeholder="明确需完善的实验室或影像学检查。"></el-autocomplete>
          </el-form-item>
          <el-form-item label="2.药物治疗：">
            <el-autocomplete type="textarea" v-model="medicine" clearable :rows="1" resize="vertical"
              class="info-textarea" :fetch-suggestions="querySearchDiagnosis"
              placeholder="药物名称、剂量、用法、疗程。"></el-autocomplete>
          </el-form-item>
          <el-form-item label="3.非药物治疗：">
            <el-autocomplete type="textarea" v-model="diagnosis" clearable :rows="1" resize="vertical"
              class="info-textarea" :fetch-suggestions="querySearchDiagnosis"
              placeholder="手术、康复训练、生活方式干预等。"></el-autocomplete>
          </el-form-item>
          <el-form-item label="4.护理/监测:">
            <el-autocomplete type="textarea" v-model="diagnosis" clearable :rows="1" resize="vertical"
              class="info-textarea" :fetch-suggestions="querySearchDiagnosis"
              placeholder="如监测生命体征、记录出入量等。"></el-autocomplete>
          </el-form-item>
          <el-form-item label="5.饮食建议：">
            <el-autocomplete type="textarea" v-model="diagnosis" clearable :rows="1" resize="vertical"
              class="info-textarea" :fetch-suggestions="querySearchDiagnosis"
              placeholder="如低盐、流质饮食等。"></el-autocomplete>
          </el-form-item>
        </el-form>
      </el-col>

      <!-- RIGHT SIDE -->
      <el-col :span="8">
        <el-form label-width="20px">
          <div>
            <!-- 这里放图 -->
            <span class="field-label">辅助检查（Auxiliary Examination）</span>
            <div class="upload-container">
              <el-upload ref="pictureUpload" :action="$baseUrl + '/files/upload'" list-type="picture-card"
                :auto-upload="true" :on-success="handleImgSuccess" :on-remove="handleRemove" :data="extraData"
                :before-upload="beforeUpload" :class="{ disabled: uploadDisabled }" class="custom-upload" multiple
                :limit="3">
                <i slot="default" class="el-icon-plus "></i>
                <div slot="file" slot-scope="{file}">
                  <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
                  <span class="el-upload-list__item-actions">
                    <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                      <i class="el-icon-zoom-in"></i>
                    </span>
                    <span v-if="!disabled" class="el-upload-list__item-delete"
                      @click="handleRemove(file, uploadedUrls)">
                      <i class="el-icon-delete"></i>
                    </span>
                  </span>
                </div>
              </el-upload>
            </div>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </div>
          <br><br><br><br><br><br><br><br>
        </el-form>

        <!-- MEDICINE -->
        <el-form inline label-width="80px" class="custom-form-inline">
          <el-form-item>
            <div class="info-field">
              <span class="field-label">选择药品:</span>
              <el-select v-model="selectedMedicine" placeholder="请选择药品" class="medicine-select">
                <div>
                  <el-option v-for="item in drugList" :label="item.drugName" :value="item.drugName"
                    :key="item.id"></el-option>
                </div>
              </el-select>

            </div>
            <div class="info-field">
              <span class="field-label">药品数量:</span>
              <el-input-number v-model="medicineQuantity" :min="1" :max="100" :step="1" placeholder="数量"
                class="quantity-input"></el-input-number>
            </div>
            <div class="info-field">
              <span class="field-label">每日次数:</span>
              <el-autocomplete class="frequency-input" v-model="medicineFrequency" :fetch-suggestions="querySearch"
                placeholder="一日几次" @select="handleSelectFrequency"></el-autocomplete>
              <el-button type="primary" @click="confirmMedicine" class="confirm-button">确定</el-button>
            </div>
          </el-form-item>
        </el-form>

          <div class="info-field">
            <span class="field-label"> 是否需要住院 : </span>
            <el-radio v-model="radio" label="是" @change="handleRadioChange()">是</el-radio>
            <el-radio v-model="radio" label="否" @change="handleRadioChange()">否</el-radio>
            <el-button type="primary" @click="ok" class="confirm-button1">诊疗结束</el-button>
          </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
export default {
  name: "Case",
  data() {
    return {
      caseInfo: { inHospital: this.radio }, // 单个病历信息
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      advice: '', // 病情
      diagnosis: '',//诊断结果
      medicine: '', // 药品
      selectedMedicine: '', // 选择的药品
      medicineQuantity: '', // 药品数量
      medicineFrequency: '', // 一日几次
      radio: '',
      tableData: [],
      drugList: [],
      receivedData: {},
      restaurantsFrequency: [],
      restaurantsAdvice: [],
      restaurantsDiagnosis: [],
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      uploadedUrls: [],
      uploadDisabled: false, // 控制上传组件的显示与隐藏
      extraData: { isTraverse: true },
      imgURL: { img: '' }
    }
  },
  created() {
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
        console.log("caseInfo", caseInfo)
      } catch (error) {
      }
    },

    async loadByUser() {
      const res = await this.$request.get('/record/selectAll', {
        params: {
          doctorId: this.user.id,
        },
      });
      this.tableData = res.data;
    },

    async loadByDrug() {
      this.$request.get("/drug/selectAll", {
        params: {
          hospitalId: this.user.hospitalId
        }
      }).then(res => {
        this.drugList = res.data
      })
    },

    updateCaseInfoFromTableData() {
      // 更新caseInfo以匹配tableData中的项 
      const matchingItem = this.tableData.find(item => item.userName === this.caseInfo.userName || item.userName === this.caseInfo.name);
      if (matchingItem) {
        // 使用解构赋值或直接赋值来更新caseInfo  
        // 注意：直接赋值整个对象可能会覆盖Vue的响应式跟踪，除非matchingItem本身是一个响应式对象  
        this.caseInfo = { ...matchingItem }; // 使用解构赋值来保持响应性  
      }
      // 如果没有找到匹配项，caseInfo将保持不变（从URL查询参数中解析的值）  
    },

    //每日次数输入框
    querySearch(queryString, cb) {
      var restaurantsFrequency = this.restaurantsFrequency;
      var results = queryString ? restaurantsFrequency.filter(this.createFilter(queryString)) : restaurantsFrequency;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurantsFrequency) => {
        return (restaurantsFrequency.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    loadFrequency() {
      return [
        { "value": "一日一次" },
        { "value": "一日两次" },
        { "value": "一日三次" },
      ];
    },
    handleSelectFrequency(item) {
    },

    //病情联想
    querySearchAdvice(queryString, cb) {
      var restaurantsAdvice = this.restaurantsAdvice;
      var results = queryString ? restaurantsAdvice.filter(this.createFilter(queryString)) : restaurantsAdvice;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurantsAdvice) => {
        return (restaurantsAdvice.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    loadAdvice() {
      return [
        { "value": "住院开药" },
        { "value": "发烧，四肢无力" },
        { "value": "咳嗽，鼻塞，咽喉红肿" },
        { "value": "腹泻" },
        { "value": "皮肤红肿" },
      ];
    },

    //诊断结果联想
    querySearchDiagnosis(queryString, cb) {
      var restaurantsDiagnosis = this.restaurantsDiagnosis;
      var results = queryString ? restaurantsDiagnosis.filter(this.createFilter(queryString)) : restaurantsDiagnosis;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurantsDiagnosis) => {
        return (restaurantsDiagnosis.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    loadDiagnosis() {
      return [
        { "value": "肺炎" },
        { "value": "发烧" },
        { "value": "流感" },
        { "value": "肠胃炎" },
        { "value": "皮肤过敏" },
      ];
    },

    //图片删除
    handleRemove(file, uploadedUrls) {
      let uploadFiles = this.$refs.pictureUpload.uploadFiles
      for (var i = 0; i < uploadFiles.length; i++) {
        if (uploadFiles[i]['url'] == file.url) {
          //服务器端删除图片
          let fileIdentifier = uploadedUrls[i];
          this.$request.delete(`/files/${fileIdentifier}`)
            .then(response => {
              uploadedUrls.splice(i, 1);
            })
            .catch(error => {
            });
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
      const uploadedUrlString = response.data;  //从上传成功后返回的Url中获取图片在服务器中的名称，并将其存在数组里
      const uploadedUrl = uploadedUrlString.split("/");
      const lastPart = uploadedUrl[uploadedUrl.length - 1];
      this.uploadedUrls.push(lastPart);
      //存入数据库的url
      if (this.imgURL.img === '') {
        this.imgURL.img = response.data + "\n";
      } else {
        this.imgURL = this.imgURL + response.data + "\n";
      }
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');

      if (!isImage) {
        this.$message.error('上传文件只能是图片格式!');
      }
      return isImage;
    },
    confirmMedicine() {
      // 确定按钮
      this.medicine = this.medicine + this.selectedMedicine + " " + this.medicineQuantity + " " + this.medicineFrequency + "\n"
      this.radio = "否"
      this.selectedMedicine = ''
      this.medicineFrequency = ''
      this.medicineQuantity = '1'
    },
    /**
     * 确认病历，并插入到数据库中
     */
    ok() {
      let newTraverse = {}
      newTraverse.userId = this.caseInfo.userId
      newTraverse.idCard = this.caseInfo.idCard
      newTraverse.timestamp = new Date().getTime()
      newTraverse.treatmentDate = this.caseInfo.time
      newTraverse.doctorId = this.user.id
      newTraverse.hospitalId = this.caseInfo.hospitalId
      this.advice == '' ? newTraverse.advice = "无" : newTraverse.advice = this.advice
      this.diagnosis == '' ? newTraverse.diagnosis = "无" : newTraverse.diagnosis = this.diagnosis
      this.medicine == '' ? this.medicine = "无" : ''
      newTraverse.drug = this.medicine
      newTraverse.inHospital = this.radio
      newTraverse.img = this.imgURL.img

      const fromPage = this.$route.query.fromPage;  //从住院业务跳转就设置该状态
      if (fromPage === 'Hospitalization') {
        newTraverse.careStatus = '未护理';
      } else {
        newTraverse.careStatus = '';
      }
      // 确认病历，上传到数据库
      this.$request.post('/traverse/add', newTraverse).then(res => {
        if (res.code === '200') {
          newTraverse.id = res.data
          newTraverse.hospitalName = this.caseInfo.hospitalName
          newTraverse.doctorName = this.caseInfo.doctorName
          newTraverse.userName = this.caseInfo.userName
          // 如果不需要住院，则跳转到加密界面
          // 进行加密、签名后，诊疗结束
          if (this.radio === '否')
            this.$router.push({ name: "CaseEncrypt", query: newTraverse })
          else
            this.$message.success("诊疗结束")
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleRadioChange() {
      this.radio = this.radio === '是' ? '是' : '否';
      if (this.radio == "是") {
        this.medicine = "住院开药"
      }
    }
  }
}
</script>

<style scoped>
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

::v-deep .el-textarea__inner{
  border: none; 
  border-bottom: 1px solid #dcdfe6; 
  border-radius: 0; 
  outline: none; 
  box-shadow: none !important; 
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
  width:50%;
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
  float: right;
  margin-top: 1px;
  margin-right: 12px;
  font-size: 16px;
}

.confirm-button1 {
  float: right;
  margin-top: 10px;
  margin-right:5px;
  font-size: 16px;
  width: 30%;
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
  /* 移除默认的右边距 */
  margin-bottom: 0;
  /* 移除默认的底边距 */
}


.upload-container {
  width: 50px;
  height: 50px;
  position: relative;
  /* 用于定位图片 */
}

::v-deep .el-upload-list--picture-card .el-upload-list__item {
  transform: scale(1);
  /*放大图像框*/
  transform-origin: top left;
  margin: 15px;
  width: 100%;
  height: 100%;
}

::v-deep .el-upload-list__item-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

::v-deep .disabled .el-upload--picture-card {
  display: none !important;
  /*上传图片后，隐藏下一个上传框 */
}
</style>
