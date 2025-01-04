<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入转诊记录id" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="outDoctorName" label="转出医生" width="80px"></el-table-column>
        <el-table-column prop="why" label="转诊原因"></el-table-column>
        <el-table-column prop="outTime" label="转出时间"></el-table-column>
        <el-table-column prop="inHospitalName" label="转入医院" show-overflow-tooltip></el-table-column>
        <el-table-column prop="result" label="结果"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" v-if="operation(scope.row)" @click="update(scope.row)">同意</el-button>
            <el-button plain type="danger" size="mini" v-if="operation(scope.row)" @click="refuse(scope.row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 进度条弹窗 -->
    <el-dialog :visible="showProgress" top="calc(100% / 4)" title="正在上传病历至区块链..." center>
        <el-progress v-if="showProgress" :percentage="progressPercentage" class="progress-demo"></el-progress>
        <div v-if="showProgress" style="font-size: 10px; left:0">正在发送: {{ sendData }}</div>
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
      status: null,
      formVisible: false,
      form: {
        out_hospital: '',
        in_hospital: '',
        out_doctor: '',
        in_doctor: '',
        out_time: '',
        in_time: '',
        why: '',
        result: ''
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        out_hospital: [
          { required: true, message: '请输入转出医院', trigger: 'blur' }
        ],
        in_hospital: [
          { required: true, message: '请输入转入医院', trigger: 'blur' }
        ],
        out_doctor: [
          { required: true, message: '请输入转出医生', trigger: 'blur' }
        ],
        in_doctor: [
          { required: true, message: '请输入转入医生', trigger: 'blur' }
        ],
        out_time: [
          { required: true, message: '请输入转出时间', trigger: 'blur' }
        ],
        in_time: [
          { required: true, message: '请输入转入时间', trigger: 'blur' }
        ],
        why: [
          { required: true, message: '请输入转诊原因', trigger: 'blur' }
        ],
        result: [
          { required: true, message: '请输入结果', trigger: 'blur' }
        ]
      },
      showProgress: false,
      progressPercentage: 0,
      sendHospital: '',
      sendData: '',
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    operation(row) {
      return this.user.role === "ADMIN" && row.result === "待审批" && this.user.hospitalId == row.outHospitalId;
    },
    /**
     * 同意转出,自动上传病历到区块链
     * @param row 转诊记录
     */
    update(row) {
      // TODO: 获取对应的病历，并将病历上传至区块链
      // 1. 获取病历
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

      // 2. 上传病历至区块链，同时显示进度条
      // 2.1 上传

      // 2.2 显示进度条
      this.$confirm('你确定要发送这份病历吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        // this.sendHospital = row.hospitalName
        this.showProgress = true;
        this.startProgress(traverse);
      }).catch(() => {
        this.$message('取消发送')
      });
    },
    refuse(row) {
      let form = {
        id: row.id,
      }
      this.$request.put('/referal/refuseOut', form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('拒绝接收')
          this.load(1)
          // this.record(row)
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    record(row) {
      let data = {
        userId: row.userId,
        doctorId: row.inDoctorId,
        hospitalId: this.user.hospitalId
      }
      this.$request.post('/record/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('数据同步成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.$confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', { type: "warning" }).then(response => {
        this.$request.delete('/referralRecord/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => { })
    },
    save() {
      this.$request.post('/referralRecord/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.load(1)
          this.formVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.form = {
        out_hospital: '',
        in_hospital: '',
        out_doctor: '',
        in_doctor: '',
        out_time: '',
        in_time: '',
        why: '',
        result: ''
      }
      this.formVisible = true
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      // 查询本医院所有的转诊记录
      this.$request.get('/referral/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          outHospitalId: this.user.hospitalId
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
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    /**
     * 显示上传病历至区块链的进度条
     * @param traverse 病历 
     */
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
          if (index <= keys.length){
            this.sendData = keys[index] + ' : ' + traverse[keys[index]]
            index++ 
          }
        }, 1000);
  
        // 模拟操作完成后，可以取消进度条显示
        setTimeout(() => {
          clearInterval(interval);
          this.showProgress = false;
          this.$message({
            message: '发送成功',
            type: 'success',
          });
        }, 11000); // 假设操作需要5秒
    },
  }
}
</script>

<style scoped></style>
