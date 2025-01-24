<template>
  <div>
    <div class="search">
      <el-select v-model="status" placeholder="请选择接诊状态" style="width: 200px">
        <el-option label="未接诊" value="未接诊"></el-option>
        <el-option label="已接诊" value="已接诊"></el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="userName" label="患者姓名"  align="center"
          show-overflow-tooltip></el-table-column>
        <el-table-column prop="outHospitalName" label="转出医院" align="center"></el-table-column>
        <el-table-column prop="outTime" label="转出时间" align="center"></el-table-column>
        <el-table-column prop="reason" label="转诊原因" align="center"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="warning" style="color: blue;" size="mini" @click=call(scope.row)
              :disabled="user.role !== 'DOCTOR'">接诊</el-button>
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
  name: "Reserve",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      status: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: [],
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    call(row) {
      let caseData = JSON.parse(JSON.stringify(row));
      caseData.result = '已接诊'
      console.log(caseData)
      // 确保caseData 数据完整，否则将污染整个数据库！！ :(
      this.$request.put('/referral/update', caseData).then(res => {
        if (res.code === '200') {
          this.$message.success('接诊成功')
          let caseinfo = {
            userName: caseData?.userName,
            doctorName: this.user.name,
            hospitalName: this.user.hospitalName,
            departmentName: this.user.departmentName,
            time: new Date().toISOString().split('T')[0],
          };
          this.load(1)
          this.$router.push({
            name: 'Case',
            query: caseinfo
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', { type: "warning" }).then(response => {
        this.$request.delete('/reserve/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/referral/selectPage', {
        params: {
          result: "已转入",
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
    disable() {
      return !this.user.role === 'DOCTOR';
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped></style>
