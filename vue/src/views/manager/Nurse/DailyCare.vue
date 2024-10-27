<template>
  <div>
    <!-- 选择病房-->
    <el-select v-model="week" style="width: 300px" placeholder="请选择病房号">
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
        <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="姓名" align="center"></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="advice" label="医嘱" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="careStatus" label="护理状态" align="center"></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" @click=call(scope.row)>未护理</el-button>
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
      input: '',
      tableData: [],
    }
  },
  created() {
    this.load(); //查询病例
  },
  methods: {
    load(){
      request.get("/DailyCare/search").then(res => {
        if(res.code === '200'){
          this.tableData = res.data;
        }else{

        }
      })
    },
    call(row) { //这里还没搞懂
      // let caseData = JSON.parse(JSON.stringify(row));
      // caseData.status = '已叫号'
      // console.log(caseData);
      // this.$request.put('/reserve/update', caseData).then(res => {
      //   if (res.code === '200') {
      //     this.$message.success('叫号成功')
      //     let caseinfo = {
      //       userName: caseData?.userName,
      //       doctorName: caseData?.doctorName,
      //       hospitalName: caseData?.hospitalName,
      //       departmentName: caseData?.departmentName,
      //       time: caseData?.time,
      //     };
      //     this.load(1)
      //     // 往就诊记录里同步一条数据
      //     this.record(row)
      //     this.$router.push(`case?data=${encodeURIComponent(JSON.stringify(caseinfo))}`)
      //   }
      // })
    },
  }
}
</script>


<style scoped>

</style>