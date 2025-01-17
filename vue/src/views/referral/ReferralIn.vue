<template>
  <div>
    <div class="search">
      <div class="search1">
        <label for="search">搜索:</label>
        <el-input id="search" placeholder="请输入关键词" style="width: 200px" v-model="keywords"></el-input>
        <el-button type="info" plain  @click="load(1)">查询</el-button>
        <el-button type="warning" plain  @click="reset">重置</el-button>
      </div>

      <div class="update">
        <label for="pull">病历地址:</label>
        <el-input id="pull" placeholder="请输入病历地址" style="width: 200px" v-model="caseAddr"></el-input>
        <el-button type="primary" plain  @click="pull">获取病历</el-button>
      </div>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="hospitalName" label="转出医院" width="200px"show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="转出医生" width="100px"></el-table-column>
        <el-table-column prop="treatmentDate" label="就诊时间" width="100px"></el-table-column>
        <el-table-column prop="referralReason" label="转诊原因"></el-table-column>
        <el-table-column prop="singResult" label="验签结果" width="100px"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template>
            <el-button plain type="danger" size="mini" v-if="user.role === 'ADMIN'">同意</el-button>
            <el-button plain type="danger" size="mini" v-if="user.role === 'ADMIN'">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>
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
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    operation(row) {
      return this.user.role === "ADMIN";
    },
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
      const Request = axios.create({
            baseURL: 'http://localhost:8088', // 区块链管理平台的 baseURL
            timeout: 50000 
          });

      Request.get('', {
        params: {
          Addr: this.caseAddr,
        }
      }).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('获取病历成功')
          this.load(1)
        } else{}
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
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
.search > .update {
margin-left: 100px;
}

</style>
