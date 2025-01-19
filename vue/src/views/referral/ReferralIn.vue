<template>
  <div>
    <div class="search">
      <div class="search1">
        <label for="search">搜索:</label>
        <el-input id="search" placeholder="请输入关键词" style="width: 200px" v-model="keywords"></el-input>
        <el-button type="info" plain @click="load(1)">查询</el-button>
        <el-button type="warning" plain @click="reset">重置</el-button>
      </div>

      <div class="update">
        <label for="pull">获取病历:</label>
        <el-input id="pull" placeholder="输入病历地址或患者姓名" style="width: 200px" v-model="caseAddr"></el-input>
        <el-button type="primary" plain @click="pull">获取病历</el-button>
      </div>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="hospitalName" label="转出医院" width="200px" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="转出医生" width="100px"></el-table-column>
        <el-table-column prop="treatmentDate" label="就诊时间" width="100px"></el-table-column>
        <el-table-column prop="referralReason" label="转诊原因"></el-table-column>
        <el-table-column prop="singResult" label="验签结果" width="100px"></el-table-column>
        <el-table-column label="操作" width="180" align="center" v-if="user.role === 'ADMIN'">
          <template>
            <el-button plain type="danger" size="mini">同意</el-button>
            <el-button plain type="danger" size="mini">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog :visible="showDialog" top="calc(100% / 4)" :title="dialogTitle" center fullscreen @close="handleClose">
      <div class="progress" v-if="showProgress">
        <el-progress :percentage="progressPercentage" class="progress-demo"></el-progress>
        <div style="font-size: 10px; left:0">正在接收: {{ recievedData }}</div>
      </div>

      <div class="verifySign" v-if="showVerifySign">
        <el-row :gutter="24">
          <el-col :span="8">
            <span style="display: inline-block; width: 200px;font-size: 16px;">环签名数据</span>
            <div class="grid-content bg-purple">
              <el-input type="textarea" :rows="2" readonly v-model="signData">
              </el-input>
            </div>
          </el-col>

          <!-- 签名信息 -->
          <el-col :span="8">
            <span style="display: inline-block; width: 200px; font-size: 16px;">环签名信息</span>
            <div class="grid-content bg-purple-light">
              <el-input type="textarea" :rows="2" readonly placeholder="请输入内容" v-model="signKey">
              </el-input>
            </div>
          </el-col>

          <!-- 验签结果 -->
          <el-col :span="8">
            <sapn style="display: inline-block; width: 200px; font-size: 16px;">验签结果</sapn>
            <div>
              <el-input type="textarea" :rows="2" readonly placeholder="请输入内容" v-model="signResult">
              </el-input>
            </div>
            <el-button class="verifyBtn" plain type="primary" @click="verifySign()">验签</el-button>
          </el-col>
        </el-row>
        <div>
          <div class="header" style="margin: 10px 0">
            <h2>签名数据区块链地址</h2>
          </div>

          <img :src="blockInfo[0].QR" width="150px" height="150px" style="display: inline-block;">
          <p
            style="color: blue; padding: 20px; border-radius: 5px; border: 2px solid rgb(235, 238, 245); font-size: 20px; display:inline-block; margin-left: 50px; position:relative;bottom:55px">
            {{ blockInfo[0].blockHash }}</p>

          <div class="header" style="margin: 10px 0">
            <h2>环公钥组成信息</h2>
          </div>

          <el-table :data="pubs" border style="width: 100%;">
            <el-table-column prop="name" align="center" label="姓名" width="180">
            </el-table-column>
            <el-table-column prop="key" align="center" label="公钥">
            </el-table-column>
          </el-table>
        </div>
      </div>

    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ReferralRecord",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      keywords: null,
      caseAddr: null,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      showDialog: false,
      showProgress: false,
      showVerifySign: false,
      progressPercentage: 0,
      recievedData: '',
      signData: '1',
      signKey: '2',
      signResult: '3',
      dialogTitle: '',
      pubs: [],
      blockInfo: [{ QR: '', blockHash: 'NULL' }]

    }
  },
  created() {
    // this.load(1)
  },
  methods: {
    /**
     * 同意转出 
     * @param row 转诊记录
     */
    update(row) {
      let form = {
        id: row.id,
      }
      this.$request.put('/referral/agreenIn', form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.load(1)
          this.record(row)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    refuse(row) {
      let form = {
        id: row.id,
      }
      this.$request.put('/referral/refuseIn', form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('拒绝接收')
          this.load(1)
          // this.record(row)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/traverse/selectPageReferralTraverse', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.status = null
      this.load(1)
    },
    /**
     * 根据给定地址，从区块链获取对应病历
     */
    pull() {
      var traverse = {
        "id": 72,
        "userId": 2,
        "doctorId": 3,
        "hospitalId": 1,
        "advice": "BHkqLP8LiF+G2K1qRJm18f71xQJLqtL9BA0qEt9PBOS4JIZCBCPBLSrODGShog3BoEgfWs62bPaRuBQNGDuRsoBgypYcA0YPjvuHgGmt6/87oIJdZnHZTJR+SeRTRxm8n7lKp6Y19dtUza1Ob9qXlfZ9ryKXRT/r+BLWQBHsKw==",
        "diagnosis": "流感",
        "drug": "BB0R3TCiAUjv/eJlFNry4mUAAbqRXRToG8uvG8ZkAJsw33WlgDBwENZfYllKWnJswC0Qd4YmSCFgTToJFswgiiTaC84duFmEayLUiVnKpQOKVuvH1DCXssgTZFyEAtvT+ej6lRi5+qp+EnIRcaEIpXk3tgioPp0hyRQUA7GwMbBWaDo=",
        "inHospital": "未住院",
        "careStatus": "是",
        "wardId": null,
        "timestamp": "1733642609746",
        "signData": "BJtLXLcWriHeVBDStq7hc0TwDX4vwwFwmumCXMl9/k2AUUjiL84OxILbJS9W9p6IBj4qCv5es7C2YwD8yURQ5ci1FyQpD3JC5tBkUTAfzY1Gd6dpo6Oxflpk8q8aQjdledXCxHLX8w==+2024-12-08 15:23:39",
        "signResult": null,
        "signPubKey": "赵千里:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABI/IhcE/TVwfnQ9kqRWP3BGHKXCWlPuTsTeXVK/aqYFJBZqAhEzXc2+hup4okD+j052rF+e2Q6rfVyF0CT+/KjQ=, 钱有有:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABAMV1h13WGHClqLdqGoXcdfg1Cx4FsYsSlwdiDiNwr3UqZCfcPSKweFrAuwhPen0e6lQS+he13wqRt3sJbhrtgE=, 王有为:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABBXcrcTGgtgYZq1bqlbngU14YJPcIlRlJjHvqH4NW7aG3TddLRRE0mhd+FjImm37Hp57rnT89eL2j6pH3kA7Qx8=, 张小明:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABE1OLem/WThDIdkKdpO6yM0kuvP7gyf0X0rMzuE3RidQSR+jAwtmVggDfrDqrCaZaPWlvPcm8eXZNG3jNikxcMs=, 李红:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABIze51HsQdV/YVjSmgL9bR1J1KmAHlS0SR4zDliKFHB3W05MznjINFdgnecuZSTWyEbYj2qBIqLunvk3U4qMyR4=, 王强:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABJAbv1/XcNYzi/uRT16Zbf+1u034/x5y2W6HPePCAMNQYJznaIQ7LeSSWvm4mO8kiBr4vrYEdavrlAaSX8BifBw=, 赵小青:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABMU4S3IPkXfFS2unwmqBdKy3tbp/Agg9HeST+fP52b82j1kHnnl52uE23nodHCNWLpnFW+9dZMLUTLdSLnivKGk=, 吴小红:MIIBMzCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBA0IABGmZq0aCc9xHKCfCBHS9d5Wf81ZHvjSxEMFfk1tQ89/4h/Gjlw2FoJv3AyRk4urGJn2nNNn0f8A0MiHLazLkIYM=",
        "signKey": "9037056148458658236958158212028595650540348742421821496829966020576577009449,95262391589953450083630478442600654780320553129704624060831202070034007492955,50703414404018240893972386549512850333514189896011496182458797451479112062743,23967270734965266663895629144229661944026949663383184060080959218903531033164,53821173226677777191000205116328609513041560282839592391950898237045084772556,31981015472273294261275145364829907494557703845912335369365084507580167813891,30093450795674958059574920863001788035012408362168308320214209727861019315989,43160427299646997721692734668342306996081318619067514400827129926836824506832,40734196741075247204517086490928115620839007057762095472003980864795026748201",
        "treatmentDate": "2024-12-08",
        "img": "",
        "isValid": "是",
        "userName": "李四",
        "doctorName": "钱有有",
        "hospitalName": "xx大学第一附属医院",
        "wardName": null
      }
      if (true) {
        this.showDialog = true
        this.dialogTitle = "正在获取病历..."
        // 1. 请求获取病历数据，同时启动进度条
        this.load(1)
        this.startProgress(traverse)

        // 2. 验签




      }
      this.showDialog = true
      this.showProgress = true
      const Request = axios.create({
        baseURL: 'http://localhost:8088', // 区块链管理平台的 baseURL
        timeout: 50000
      });

      Request.get('', {
        params: {
          Addr: this.caseAddr,
        }
      }).then(res => {
        if (res.code === '200') {
          this.$request.put("/traverse/insertReferralTraverse", res.data.referralTraverse).then(res1 => {
            if (res1.code === '200') {
              this.$message.success('更新数据成功')
            } else {
              this.$message.error("更新数据失败")
            }
          })
          // this.tableData = res.data.returenObject[0].split("\n")
          // TODO: 获取病历成功后，需要解析数据并赋值到对应的表单中
          this.showProgress = false
          this.showVerifySign = true
        } else {
          this.showDialog = false
          this.$message.error("获取病历失败")
        }
      })
    },
    verifySign() {

    },
    startProgress(traverse) {
      var index = 0
      const keys = Object.keys(traverse)
      this.progressPercentage = 0
      let interval = setInterval(() => {
        if (this.progressPercentage >= 100) {
          clearInterval(interval);
        } else {
          this.progressPercentage += 10;
        }
        if (index <= keys.length) {
          this.sendData = keys[index] + ' : ' + traverse[keys[index]]
          index++
        }
      }, 1000);

      // 模拟操作完成后，可以取消进度条显示, 并显示验签页面
      setTimeout(() => {
        clearInterval(interval);
        this.showProgress = false;
        this.showVerifySign = true
        this.dialogTitle = "环签名验证"
        this.$message({
          message: '发送成功',
          type: 'success',
        });
      }, 1100); // 假设操作需要5秒
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleClose() {
      this.showDialog = false
    },
  }
}
</script>

<style scoped>
.search * {
  margin-right: 10px;
  display: inline-block;
}

.search label {
  margin-right: 5px;
}

.search>.update {
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
