<template>
  <el-card class="case-container">
    <el-row class="info-row" :gutter="24">
      <el-col :span="12">
        <el-form inline label-width="80px">
          <el-form-item class="field-container">
            <span class="field-label">姓名:</span>
            <span class="field-value">{{ caseInfo.userName }}</span>
          </el-form-item>
        </el-form>
        <!-- <hr class="divider" /> -->
        <el-form inline label-width="80px" class="custom-form-inline">
          <el-form-item>
            <div class="field-container">
              <span class="field-label">病情:</span>
              <el-autocomplete type="textarea" v-model="advice" clearable :rows="1" resize="vertical"
                class="info-textarea" :fetch-suggestions="querySearchAdvice"></el-autocomplete>
            </div>
            <div class="field-container">
              <span class="field-label">诊断结果:</span>
              <el-autocomplete type="textarea" v-model="diagnosis" clearable :rows="1" resize="vertical"
                class="info-textarea" :fetch-suggestions="querySearchDiagnosis"></el-autocomplete>
            </div>
            <!-- <div class="info-field">
              <span class="field-label"> 是否需要住院 : </span>
              <el-radio v-model="radio" label="是" @change="handleRadioChange()">是</el-radio>
              <el-radio v-model="radio" label="否" @change="handleRadioChange()">否</el-radio>
            </div> -->
          </el-form-item>
        </el-form>
        <!-- <hr class="divider" /> -->
        <el-form inline label-width="80px" class="custom-form-inline">
          <el-form-item>
            <div class="info-field">
              <!-- <span class="field-label">药品信息:</span> -->
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
            <div>
              <el-input type="textarea" v-model="medicine" clearable :rows="2" resize="vertical"
                class="medicine-textarea"></el-input>
            </div>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <el-form label-width="20px">
          <el-form-item>
            <!-- 这里放图 -->
            <span class="field-label">医疗影像:</span>
            <div class="upload-container">
              <el-upload ref="pictureUpload" :action="$baseUrl + '/files/upload'" list-type="picture-card"
                :auto-upload="true" :on-success="handleImgSuccess" :on-remove="handleRemove" :data="extraData"
                :before-upload="beforeUpload" :class="{ disabled: uploadDisabled }" class="custom-upload" multiple
                :limit="3">
                <i slot="default" class="el-icon-plus"></i>
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
          </el-form-item>
          
          <br><br><br><br><br><br><br><br>
          <!-- <br> -->
          <!-- <br><br><br><br><br> -->
          <!-- <br><br><br><br><br> 空白占位，不要删 -->

          <el-button type="primary" @click="ok" class="confirm-button1">诊疗结束</el-button>
          <div class="info-field">
            <br>
              <span class="field-label"> 是否需要住院 : </span>
              <el-radio v-model="radio" label="是" @change="handleRadioChange()">是</el-radio>
              <el-radio v-model="radio" label="否" @change="handleRadioChange()">否</el-radio>

            </div>

        </el-form>
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
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleRadioChange() {
      this.radio = this.radio === '是' ? '是' : '否';
      if (this.radio == "是") {
        this.medicine = "无"
      }
    }
  }
}
</script>

<style scoped>
.custom-col {
  margin-left: 0px;
  /* 减小左侧间距 */
  margin-right: 0px;
  /* 减小右侧间距 */
}

.case-container {
  padding: 0px;
  height: 100%;
}

.case-header {
  margin-bottom: 20px;
  text-align: center;
}

.info-row {
  margin-top: 15px;
  margin-bottom: 6px;
}

.info-field {
  margin-bottom: 15px;
}

.field-label {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  margin-right: 10px;
  width: 80px;
  font-weight: bold;
}


.field-value {
  margin-left: 10px;
  margin-right: 150px;
}

.info-textarea {
  margin-bottom: 26px;
  width: 220px;
  height: 20px
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

.custom-form-inline .el-form-item {
  margin-right: 0;
  /* 移除默认的右边距 */
  margin-bottom: 0;
  /* 移除默认的底边距 */
}

.field-container {
  display: flex;
  align-items: flex-start;
  /* 根据需要调整垂直对齐方式 */
}

.upload-container {
  width: 145px;
  height: 145px;
  position: relative;
  /* 用于定位图片 */
}

::v-deep .el-upload--picture-card {
  transform: scale(2);
  /*放大上传框*/
  transform-origin: top left;
  margin: 15px;
  width: 100%;
  height: 100%;
  position: absolute;
  /* 绝对定位，使图片可以覆盖整个容器 */
  top: 0;
  left: 0;
}

::v-deep .el-upload-list--picture-card .el-upload-list__item {
  transform: scale(2);
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

::v-deep .el-textarea__inner {
  color: blue;
  font-size: 16px;
}
</style>
