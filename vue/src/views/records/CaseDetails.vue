<template>
  <el-card class="case-details">
    <div class="header">
      <h2>病例详情</h2>
    </div>
    <el-row :gutter="24">
      <el-col :span="9">
        <el-form label-width="10px">
          <el-form-item>
            <!-- 这里放图 -->
            <span class="field-label">医疗影像:</span>
            <div class="image-container">
              <div @click.stop="previewImage(url, index)" v-for="(url, index) in ImageLines" :key="index">
                <div class="demo-image" @click="previewImage(url, index)">
                  <el-image style="width: 350px; height: 350px" :src="url" :fit="fit"></el-image>
                </div>
              </div>
            </div>
            <el-dialog :visible.sync="dialogVisible" size="50%">
              <img width="100%" :src="previewImageUrl" :alt="'Preview of ' + (previewImageIndex + 1)" />
            </el-dialog>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="15">
        <el-form label-width="100px">
          <el-form-item label="患者姓名" class="custom-layout">
            <div class="content-wrapper">
              <span class="name">{{ receivedData.name }}</span>
              <el-button type="primary" class="decrypt-button" @click="decryptAdviceAndDrug">解密</el-button>
            </div>
          </el-form-item>
        </el-form>
        <el-form label-width="100px">
          <el-form-item label="医嘱信息">
            <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 4 }" placeholder="请输入内容"
              v-model="receivedData.advice">
            </el-input>
          </el-form-item>
          <el-form-item label="药品信息">
            <el-table :data="drug" style="width: 100%" height="200" border>
              <el-table-column prop="name" label="药品名称"></el-table-column>
              <el-table-column prop="dose" label="数量"></el-table-column>
              <el-table-column prop="frequency" label="用法用量"></el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="8">
        <div style="margin: 0 0 20px 0">

          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                环签名数据
              </div>
            </el-col>
            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button plain type="primary" @click="sign()" v-if="user.role === 'DOCTOR'">签名</el-button>
              </div>
            </el-col>
          </el-row>

        </div>
        <div class="grid-content bg-purple">
          <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 8 }" placeholder="请输入内容"
            v-model="receivedData.signData">
          </el-input>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="margin: 0 0 34px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                环签名信息
              </div>
            </el-col>
          </el-row>

        </div>
        <div class="grid-content bg-purple-light">
          <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 8 }" placeholder="请输入内容"
            v-model="receivedData.signKey">
          </el-input>
        </div>
      </el-col>
      <el-col :span="8">
        <div style="margin: 0 0 34px 0">
          <el-row>
            <el-col :span="10">
              <div style="margin: 5px 0 0 25px">
                验签结果
              </div>
            </el-col>
            <el-col :span="14">
              <div class="grid-content bg-purple-light">
                <el-button plain type="primary" @click="verifySign()" v-if="user.role === 'USER'">验签</el-button>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="grid-content bg-purple-light">
          <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 8 }" placeholder="请输入内容"
            v-model="receivedData.signResult">
          </el-input>
        </div>
      </el-col>
    </el-row>

    <div class="header" style="margin: 20px 0">
      <h2>环公钥组成信息</h2>
    </div>

    <div v-if="pubs.length !== 0">
      <el-table :data="pubs" height="250" border style="width: 100%">
        <el-table-column prop="name" label="姓名" width="180">
        </el-table-column>
        <el-table-column prop="key" label="公钥">
        </el-table-column>
      </el-table>
    </div>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      receivedData: [],
      params: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      pubs: {},
      fits: 'fill',
      dialogVisible: false, // 对话框可见性  
      previewImageUrl: '', // 预览图片URL  
      previewImageIndex: -1 // 预览图片索引 
    };
  },
  created() {
    const queryData = this.$route.query.data;
    if (queryData) {
      this.receivedData = JSON.parse(decodeURIComponent(queryData));
    }
    else {
      this.receivedData = this.$route.params.inform;
      console.log(this.receivedData)
    }
    this.load()
  },
  computed: {
    drug() {
      const medicationString = this.receivedData.drug;
      // 拆分字符串为每一行
      const lines = medicationString.split('\n');
      // 将每一行拆分为药物信息对象
      return lines.map(line => {
        const parts = line.split(' ');
        return {
          name: parts[0],
          dose: parts[1],
          frequency: parts[2]
        };
      });
    },
    ImageLines() {
      const urlImageString = this.receivedData.img;
      const urlImageLines = urlImageString.split('\n');
      if (urlImageLines.length > 0 && urlImageLines[urlImageLines.length - 1] === '') {
        urlImageLines.pop();
      }
      return urlImageLines;
    }
  },
  methods: {
    //图片预览
    previewImage(url, index) {
      this.previewImageUrl = url;
      this.previewImageIndex = index;
      this.dialogVisible = true;
    },

    load() {
      if (this.receivedData.length) {
        this.$router.push("/caseList")
      }

      if (this.receivedData.signPubKey !== null || this.receivedData.signPubKey !== "") {
        const s = this.receivedData.signPubKey.split(",")
        const ss = s.map(line => {
          const parts = line.split(':');
          return {
            name: parts[0],
            key: parts[1],
          };
        });
        this.pubs = ss
      }
    },
    /**
     * 数据解密
     */
    decryptAdviceAndDrug() {
      let params = {
        name: this.receivedData.name,
        advice: this.receivedData.advice,
        drug: this.receivedData.drug
      }
      this.$request.post('keys/decrypt', params).then(res => {
        if (res.code === '200') {
          this.receivedData.advice = res.data.advice
          this.receivedData.drug = res.data.drug
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    sign() {
      console.log(this.receivedData)
      this.params.role = this.user.role
      this.params.id = this.receivedData.id
      this.params.name = this.receivedData.name
      this.$request.post('/keys/sign', this.receivedData).then(res => {
        if (res.code === '200') {
          // this.$message.success('成功')
          this.receivedData.signData = res.data.signData
          this.receivedData.signKey = res.data.signKey
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    verifySign() {
      this.params.role = this.user.role
      this.params.id = this.receivedData.id
      this.params.name = this.receivedData.name
      this.params.signKey = this.receivedData.signKey
      this.$request.post('/keys/verifySign', this.receivedData).then(res => {
        if (res.code === '200') {
          // this.$message.success('成功')
          this.receivedData.signResult = res.data.message
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
};
</script>

<style scoped>
.case-details {
  margin: 20px;
}

.header {
  margin-bottom: 20px;
}

.custom-layout .content-wrapper {
  display: flex;
  align-items: center;
  /* 垂直方向上居中对齐 */
  justify-content: space-between;
  /* 水平方向上两端对齐 */
  width: 100%;
  /* 确保容器宽度充满父元素 */
}

.custom-layout .decrypt-button {
  margin-left: auto;
  /* 利用自动外边距将按钮推至最右侧 */
}

.image-container {
  display: flex;
  /* 启用flexbox布局 */
  flex-wrap: wrap;
  /* 若图片数量超出容器宽度，则自动换行 */
  gap: 10px;
  /* 图片间的间距，可根据需求调整 */
}
</style>
