<template>
  <div class="case-container">
    <div class="case-header">
      <h2 style="margin: 0; text-align: left;">病历详情</h2>
    </div>

    <el-row class="info-row" :gutter="24">
      <el-col :span="12" >
        <el-form inline label-width="80px">
          <el-form-item class="field-container">
            <span class="field-label">姓名:</span>
            <span class="field-value">{{ caseInfo.userName }}</span>
          </el-form-item>
        </el-form>
        <hr class="divider" />
        <el-form inline label-width="80px" class="custom-form-inline">
          <el-form-item>
            <div class="field-container">
              <span class="field-label">病情:</span>
              <el-autocomplete type="textarea" v-model="advice" clearable :rows="5" resize="vertical"
                class="info-textarea" :fetch-suggestions="querySearchAdvice"></el-autocomplete>
            </div>
          </el-form-item>
        </el-form>
        <hr class="divider" />
        <el-form inline label-width="80px" class="custom-form-inline">
          <el-form-item>
            <span class="field-label">药品信息:</span>
            <div class="info-field">
              <span class="field-label">选择药品:</span>
              <el-select v-model="selectedMedicine" placeholder="请选择药品" class="medicine-select">
                <div>
                  <el-option v-for="item in drugList" :label="item.drugName" :value="item.drugName"
                    :key="item.id"></el-option>
                </div>
              </el-select>
              <span class="field-label">药品数量:</span>
              <el-input-number v-model="medicineQuantity" :min="1" :max="100" :step="1" placeholder="数量"
                class="quantity-input"></el-input-number>
            </div>
            <div class="info-field">
              <span class="field-label">每日次数:</span>
              <el-autocomplete class="frequency-input" v-model="medicineFrequency" :fetch-suggestions="querySearch"
                placeholder="一日几次" @select="handleSelect"></el-autocomplete>
              <el-button type="primary" @click="confirmMedicine" class="confirm-button">确定</el-button>
            </div>
            <div>
              <el-input type="textarea" v-model="medicine" clearable :rows="5" resize="vertical"
                class="medicine-textarea"></el-input>
            </div>
            <div class="info-field">
              <span class="field-label"> 是否需要住院 : </span>
              <el-radio v-model="radio" label="是" @change="handleRadioChange()">是</el-radio>
              <el-radio v-model="radio" label="否" @change="handleRadioChange()">否</el-radio>
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
                :before-upload="beforeUpload" :class="{ disabled: uploadDisabled }" class="custom-upload" :limit="1">
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
        </el-form>
      </el-col>
    </el-row>
    <el-row class="info-row" :gutter="24">
      <el-col :span="15"> 
        <el-button type="primary" @click="ok" class="confirm-button1">确定</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Case",
  data() {
    return {
      caseInfo: { inhospital: this.radio }, // 单个病历信息
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
      receivedData: {},
      restaurants: [],
      restaurantsAdvice: [],
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      uploadedUrls: [],
      uploadDisabled: false, // 控制上传组件的显示与隐藏
      extraData: { isTraverse: true },
    }
  },
  created() {
    this.loadData(); // 加载数据tableData和初始化caseInfo的通用方法 
    this.loadByDrug(); //获取drugList
  },
  mounted() {
    this.restaurants = this.loadAll();
    this.restaurantsAdvice = this.loadAllAdvice();
  },
  methods: {
    async loadData() {
      try {
        // loadByUser返回Promises  
        await Promise.all([this.loadByUser()]);

        // 从URL查询参数中解析caseInfo  
        const queryData = this.$route.query.data;
        console.log("queryData", queryData)
        if (queryData) {
          this.caseInfo = JSON.parse(decodeURIComponent(queryData));

          // 检查tableData以更新caseInfo
          this.updateCaseInfoFromTableData();
        }
      } catch (error) {
        console.error('Error loading data:', error);
      }
    },

    async loadByUser() {
      const res = await this.$request.get('/record/selectAll', {
        params: {
          doctorId: this.user.id,
        },
      });
      this.$message.success("成功");
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
      const matchingItem = this.tableData.find(item => item.userName === this.caseInfo.userName);
      if (matchingItem) {
        // 使用解构赋值或直接赋值来更新caseInfo  
        // 注意：直接赋值整个对象可能会覆盖Vue的响应式跟踪，除非matchingItem本身是一个响应式对象  
        this.caseInfo = { ...matchingItem }; // 使用解构赋值来保持响应性  
      }
      // 如果没有找到匹配项，caseInfo将保持不变（从URL查询参数中解析的值）  
    },

    //每日次数输入框
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
    loadAll() {
      return [
        { "value": "一日一次" },
        { "value": "一日两次" },
        { "value": "一日三次" },
      ];
    },
    handleSelect(item) {
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
    loadAllAdvice() {
      return [
        { "value": "发烧" },
        { "value": "感冒" },
        { "value": "腹泻" },
        { "value": "过敏" },
      ];
    },
    handleSelect(item) {
    },

    //图片上传
    handleRemove(file, uploadedUrls) {
      let uploadFiles = this.$refs.pictureUpload.uploadFiles
      for (var i = 0; i < uploadFiles.length; i++) {
        if (uploadFiles[i]['url'] == file.url) {
          //服务器端删除图片
          let fileIdentifier = uploadedUrls[i];
          this.$request.delete(`/files/${fileIdentifier}`)
          .then(response => {
            uploadedUrls.splice(i,1);
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
      if (this.information.img === undefined) {
        this.information.img = response.data + "\n";
      } else {
        this.information.img = this.information.img + response.data + "\n";
      }
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');

      if (!isImage) {
        this.$message.error('上传文件只能是图片格式!');
      }
      return isImage;
    },

    // loadCaseInfo() {
    //   this.caseInfo = {
    //     id: '001',
    //     number: '123456',
    //     name: '默认',
    //     doctorName: '李医生',
    //     hospitalName: 'XX医院',
    //     status: '未叫号', // Assuming default status
    //   }
    // },
    // call(caseInfo) {
    //   let reserveData = { ...caseInfo, status: '已叫号' };
    //   this.$message.success('叫号成功')
    //   this.loadCaseInfo()
    //   this.record(caseInfo)
    // },
    // record(caseInfo) {
    //   let data = {
    //     userId: caseInfo.userId,
    //     doctorId: caseInfo.doctorId,
    //   }
    //   this.$message.success('数据同步成功')
    // },
    // del(id) {
    //   // Simulating confirmation dialog and API request to delete case
    //   this.$confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', { type: "warning" }).then(response => {
    //     this.$message.success('操作成功')
    //     this.loadCaseInfo()
    //   }).catch(() => { })
    // },
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
      console.log("caseInfo", this.caseInfo)
      this.information.number = new Date().getTime()
      this.information.userDate = this.caseInfo.time
      this.information.name = this.caseInfo.userName
      this.information.doctorId = this.caseInfo.doctorId
      this.information.hospitalId = this.caseInfo.hospitalId
      this.information.advice = this.advice
      this.information.drug = this.medicine
      if (this.radio == "是") {
        this.information.inhospital = "待住院"
      } else {
        this.information.inhospital = "未住院"
      }
      this.information.jurisdiction = "允许"
      this.information.signData = " "
      this.information.signResult = " "
      this.information.signPubKey = " "
      this.information.signKey = " "
      this.$request.post('/traverse/add', this.information).then(res => {
        if (res.code === '200') {
          this.$message.success('插入成功')
          this.information.hospitalName = this.caseInfo.hospitalName
          this.information.doctorName = this.caseInfo.doctorName
          let caseData = JSON.parse(JSON.stringify(this.information))
          this.$router.push(`CaseDetails?data=${encodeURIComponent(JSON.stringify(caseData))}`)
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleRadioChange() {
      this.radio = this.radio === '是' ? '是' : '否';
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
  margin-top: 15px;
  margin-bottom: 6px;
}

.info-field {
  margin-bottom: 15px;
}

.field-label {
  margin-right: 10px;
  width: 40px;
  font-weight: bold;
}


.field-value {
  margin-left: 10px;
  margin-right: 150px;
}

.info-textarea {
  margin-bottom: 26px;
  width: 570px
}

.medicine-select {
  width: 35%;
  margin-right: 40px;
}

.quantity-input {
  width: 34%;
  margin-right: 10px;
}

.frequency-input {
  width: 35%;
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
  font-size: 12px;
}

.confirm-button1 {
  float: right;
  margin-top: -10px;
  margin-right: 12px;
  font-size: 12px;
  width: 40%;
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
  position: relative; /* 用于定位图片 */
}

::v-deep .el-upload--picture-card {  
  transform: scale(3.5); /*放大上传框*/
  transform-origin: top left;
  margin: 15px;
  width: 100%;
  height: 100%; 
  position: absolute; /* 绝对定位，使图片可以覆盖整个容器 */
  top: 0;
  left: 0;
}

::v-deep .el-upload-list--picture-card .el-upload-list__item{  
  transform: scale(3.5); /*放大图像框*/
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
  display: none !important; /*上传图片后，隐藏下一个上传框 */
}
</style>
