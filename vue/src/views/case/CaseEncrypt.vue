<template>
  <el-card class="container">
    <div class="header">
      <h2>病历加密</h2>
    </div>

    <el-row :gutter="24">
      <!-- 这里放图 -->
      <el-col :span="9" style="display: flex; justify-content: center">
        <el-form label-width="10px">
          <el-form-item>
            <span class="field-label" style="display: flex; justify-content: center"
              ><strong>医疗影像</strong></span
            >
            <div class="image-container">
              <div
                @click.stop="previewImage(url, index)"
                v-for="(url, index) in ImageLines"
                :key="index"
              >
                <div class="demo-image" @click="previewImage(url, index)">
                  <el-image
                    style="width: 250px; height: 250px"
                    :src="receivedData.img"
                    :fit="fits"
                  ></el-image>
                </div>
              </div>
            </div>
            <el-dialog :visible.sync="dialogVisible">
              <img
                width="100%"
                :src="previewImageUrl"
                :alt="'Preview of ' + (previewImageIndex + 1)"
              />
            </el-dialog>
          </el-form-item>
          <!-- 二维码 -->
          <el-form-item>
            <span class="field-label" style="display: flex; justify-content: center"
              ><strong>区块链存储地址</strong></span
            >
            <div class="image-container">
              <div
                @click.stop="previewImage(url, index)"
                v-for="(url, index) in ImageLines"
                :key="index"
              >
                <div class="demo-image" @click="previewImage(url, index)">
                  <el-image
                    style="width: 170px; height: 170px"
                    :src="QR"
                    :fit="fits"
                  ></el-image>
                </div>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="15">
        <el-form label-width="100px">
          <el-form-item label="患者姓名" class="custom-layout">
            <div class="content-wrapper">
              <span class="name">{{ receivedData.userName }}</span>
              <el-button
                type="primary"
                class="decrypt-button"
                @click="encrypt"
                style="opacity: 0"
                >加密</el-button
              >
              <!-- 隐藏加密按钮 -->
            </div>
          </el-form-item>
        </el-form>
        <el-form label-width="100px">
          <el-form-item label="医嘱信息">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 2 }"
              placeholder="请输入内容"
              class="blue-text"
              v-model="receivedData.advice"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="药品信息">
            <el-table
              :data="drug"
              style="width: 100%"
              height="200"
              border
              class="blue-text"
            >
              <el-table-column prop="name" label="药品名称"></el-table-column>
              <el-table-column prop="dose" label="数量"></el-table-column>
              <el-table-column prop="frequency" label="用法用量"></el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="存储地址" class="blue-text">
            <p
              style="
                color: blue;
                border: solid 1px #ebeef5;
                padding: 5px;
                overflow-x: hidden;
              "
            >
              {{ transactionHash }}
            </p>
          </el-form-item>
          <el-form-item label="加密公钥" class="blue-text">
            <p
              style="
                color: blue;
                border: solid 1px #ebeef5;
                padding: 5px;
                overflow-x: scroll;
              "
            >
              {{ user.publicKey }}
            </p>
          </el-form-item>
        </el-form>
        <el-button
          type="primary"
          class="submit-button"
          @click="gotoSign"
          style="opacity: 0"
          >去签名</el-button
        >
        <!-- 隐藏签名按钮 -->
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      /** 接收查询参数 */
      receivedData: {},
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      pubs: {},
      fits: "fill",
      dialogVisible: false, // 对话框可见性
      previewImageUrl: "", // 预览图片URL
      previewImageIndex: -1, // 预览图片索引
      /** 区块链存储地址 */
      transactionHash: "NULL",
      QR: "",
      params: {},
      __signKey: '',
      __signPubKey: '',
      __signData: '',
    };
  },
  created() {
    this.receivedData = this.$route.query;
    this.load();
    // this.autoOp()
  },
  computed: {
    drug() {
      const medicationString = this.receivedData.drug;
      // 拆分字符串为每一行
      let lines = medicationString.split("\n");
      if (lines.length != 1) lines = lines.slice(0, -1);
      // 将每一行拆分为药物信息对象
      return lines.map((line) => {
        const parts = line.split(" ");
        return {
          name: parts[0],
          dose: parts[1] ? parts[1] : parts[0],
          frequency: parts[2] ? parts[2] : parts[0],
        };
      });
    },
    ImageLines() {
      const urlImageString = this.receivedData.img;
      const urlImageLines = urlImageString.split("\n");
      if (urlImageLines.length > 0 && urlImageLines[urlImageLines.length - 1] === "") {
        urlImageLines.pop();
      }
      return urlImageLines;
    },
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
        this.$router.push("/caseList");
      }

      if (this.receivedData.signPubKey != null) {
        const s = this.receivedData.signPubKey.split(",");
        const ss = s.map((line) => {
          const parts = line.split(":");
          return {
            name: parts[0],
            key: parts[1],
          };
        });
        this.pubs = ss;
      }
    },
    
    async upChain() {
      // 在加密之后，上链之前，进行了签名，签名页面什的数据没有保存
      this.params.id = this.receivedData.id
      await this.$request.post('/keys/sign', this.params).then(res => {
        if (res.code === '200') {
          this.__signKey = res.data.signKey
          this.__signData = res.data.signData
          this.__signPubKey = res.data.signPubKey
        } else {
          this.$message.error(res.msg)
        }
      })

      // 构造要上传的至区块链的 JSON 病历
      let traverse = {
        _idCard: this.receivedData.idCard,
        _userName: this.receivedData.userName,
        _hospitalDoctor:
          this.receivedData.hospitalName + "-" + this.receivedData.doctorName,
        _drugAdvice: this.receivedData.drug + "-" + this.receivedData.advice,
        _diagnosis: this.receivedData.diagnosis,
        _inHospital: this.receivedData.inHospital,
        _timestamp: this.receivedData.timestamp,
        _signKey: this.__signKey,
        _treatmentDate: this.receivedData.treatmentDate,
        _img: this.receivedData.img,
      };
      console.log(traverse);
      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });

      // 上传区块链
      Request.post("/storeMedicalRecord", traverse).then((res) => {
        if (res.data.code === "200") {

          this.transactionHash = res.data.data.transactionReceipt.transactionHash;
          this.$request
            .get("/files/generateQR", {
              params: {
                seed: this.transactionHash,
              },
            })
            .then((res) => {
              if (res.code === "200") {
                this.QR = res.data;
              }
            });
        } else {
          this.$message.error("上传区块链失败 :(");
        }
      });
    },
    /**
     * 加密病历文字字段
     */
    encrypt() {
      console.log("reci", this.receivedData)
      this.$request
        .patch("keys/encrypt", {
          id: this.receivedData.id,
          userId: this.receivedData.userId,
          advice: this.receivedData.advice,
          drug: this.receivedData.drug,
          userName: this.receivedData.userName,
        })
        .then((res) => {
          if (res.code === "200") {
            // 更新数据为加密后的内容
            this.receivedData.advice = res.data.advice;
            this.receivedData.drug = res.data.drug;
            this.receivedData.userName = res.data.userName;
            this.upChain();
          }
        });
    },
    gotoSign() {
      this.receivedData.signPubKey = this.__signPubKey;
      this.receivedData.signKey = this.__signKey;
      this.receivedData.signData = this.__signData;
      this.receivedData.QR = this.QR;
      this.receivedData.transactionHash = this.transactionHash;
      this.$router.push({ name: "CaseSign", query: this.receivedData });
    },
    autoOp() {
      // 模拟自动加密并跳转页面
      this.$message.info(`正在加密病历...`);
      setTimeout(() => {
        this.encrypt();
        this.$message.success(`加密成成功，即将跳转到签名页面...`);
        setTimeout(() => {
          this.$router.push({ name: "CaseSign", query: this.receivedData });
        }, 2000);
      }, 2000);
    },
  },
};
</script>

<style scoped>
.container {
  padding: 0px;
  height: 100%;
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
  overflow-x: hidden;
  color: blue;
}

.name {
  width: 70%;
  overflow-x: hidden;
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
  justify-content: center;
}

::v-deep .el-textarea__inner {
  color: blue;
}
</style>
