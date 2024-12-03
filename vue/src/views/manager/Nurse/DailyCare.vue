<template>
  <div>
    <!-- 选择病房-->
    <el-select v-model="wardId" style="width: 300px" placeholder="请选择病房号">
      <el-option label="重症监护室" value="重症监护室"></el-option>
      <el-option label="A号房" value="A号房"></el-option>
      <el-option label="B号房" value="B号房"></el-option>
      <el-option label="C号房" value="C号房"></el-option>
    </el-select>

    <!--  输入姓名   -->
    <el-input v-model="input" style="width: 300px; margin-left: 10px" placeholder="请输入姓名"></el-input>

    <el-button type="primary" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
    <el-button type="success" plain style="margin-left: 10px" @click="reset">重置</el-button>

    <div class="table" style="margin-top: 15px">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column prop="userName" label="姓名" align="center"></el-table-column>
        <el-table-column prop="wardName" label="病房号" align="center"></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="advice" label="医嘱" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="careStatus" label="护理状态" align="center"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" v-if="scope.row.careStatus === '未护理'"  @click=submit(scope.row)>护理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "DailyCare",
  data() {
    return {
      wardId: null,
      input: '',
      tableData: [],
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(); //查询病例
  },
  methods: {
    load(){
      let id = this.user.hospitalId
      this.$request.get('/DailyCare/SearchByHId/' + id).then(res => {
        if (res.code === '200') {
          this.tableData = res.data
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    submit(row){
      let caseData = JSON.parse(JSON.stringify(row));
      caseData.careStatus = '已护理'
      console.log(caseData);
      this.$request.put('/DailyCare/update', caseData).then(res => {
        if (res.code === '200') {
          this.$message.success('护理完毕')
          this.load();
        }
      })
    },
    reset(){

    },
    handleSelectionChange(){},

  }
}
</script>


<style scoped>

</style>