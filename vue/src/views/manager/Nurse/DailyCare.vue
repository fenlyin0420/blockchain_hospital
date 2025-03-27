<template>
  <div>
    <div class="search">
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
    </div>

    <div class="tables-container" style="display: flex; justify-content: space-between; width: 100%;">
      <div class="table" style="width: 40%; margin-right: 10px;">
        <el-row :gutter="24">
          <el-col :span="8" v-for="item in BedData" :key="item.id">
            <div :style="{ textAlign: 'center', backgroundColor: getBackgroundColor(item.wardName) }" class="card">
              <div class="value">{{ item.wardName }}</div>
              <div class="value">{{ item.bedName }}</div>
              <div class="value">{{ item.status }}</div>
              <!-- <div v-for="item2 in tableData" :key="item2.id">
                <div class="value" v-if="item.status === item2.userName">
                  {{ item2.advice }}
                </div>
              </div>
              <div class="value" v-if="item.status === '空'">
                {{ "当日医嘱" }}
              </div> -->
              <div style="margin-top: 15px">
                <el-button type="danger" size="mini" :disabled="disabled(item.status)"
                  @click="submit(item.status)">护理完毕</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
        <div class="pagination">
          <el-pagination background @current-change="handleCurrentChangeBed" :current-page="pageNumBed"
            :page-sizes="[5, 10, 20]" :page-size="pageSizeBed" layout="total, prev, pager, next" :total="totalBed">
          </el-pagination>
        </div>
      </div>

      <div class="table" style="width: 60%;">
        <div class="table" style="margin-top: 0px">
          <el-table :data="tableData" strip @selection-change="handleSelectionChange">
            <el-table-column prop="userName" label="姓名" align="center" width="80px"></el-table-column>
            <!-- <el-table-column label="病床号" align="center">
              <template v-slot="scope">
                {{ scope.row.wardName }}-{{ scope.row.bedName }}
              </template>
            </el-table-column> -->
            <!-- <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip align="center"></el-table-column> -->
            <el-table-column prop="traverse.drug" label="当日医嘱" show-overflow-tooltip align="center" ></el-table-column>
            <!-- <el-table-column prop="careStatus" label="护理状态" align="center"></el-table-column> -->
            <!-- <el-table-column label="操作" width="180" align="center">
              <template v-slot="scope">
                <el-button plain type="danger" size="mini" v-if="scope.row.careStatus === '未护理'"
                  @click=submit(scope.row)>护理</el-button>
              </template>
            </el-table-column> -->
          </el-table>
          <div class="pagination">
            <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
              :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "DailyCare",
  data() {
    return {
      pageNum: 1,
      pageSize: 10,
      total: 0,
      pageNumBed: 1,
      pageSizeBed: 9,
      totalBed: 0,
      wardId: null,
      input: '',
      tableData: [],
      BedData:[],
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1); //查询病例
    this.loadBed(1);//查询床位
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      // 获取系统当前日期（格式化为 YYYY-MM-DD）
      const currentDate = new Date().toISOString().slice(0, 10);
      this.$request.get('/DailyCare/selectPage',
        {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            hospitalId: this.user.hospitalId
          }
        },).then(res => {
          if (res.code === '200') {
            this.tableData = res.data?.list.filter(item => {
              return item.traverse.treatmentDate === currentDate;
            });
            this.total = res.data?.total;
          } else {
            this.$message.error(res.msg)
          }
        })
    },
    loadBed(pageNumBed){
      if (pageNumBed) this.pageNumBed = pageNumBed;
      this.$request.get('/AssignBeds/selectBedPage', {
        params: {
          pageNum: this.pageNumBed,
          pageSize: this.pageSizeBed,
          wardName: this.wardName,
        },
      }).then(res => {
        let Data = res.data?.list;
        this.BedData = Data.map(item => {
          if (item.status === '') {
            item.status = item.status === '' ? '空' : item.status;
          }
          return item;
        });
        this.totalBed = res.data?.total;
      });
    },
    submit(status) {
      let Data = this.tableData.filter(item =>item.userName === status) //找出床位对应的患者
      let caseData = Data[0];
      caseData.careStatus = '已护理';
      caseData.id = caseData.traverse.id;
      this.$request.put('/DailyCare/update', caseData).then(res => {
        if (res.code === '200') {
          this.$message.success('护理完毕')
          this.load(1);
        }
      })
    },
    reset() {

    },
    handleSelectionChange() { },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleCurrentChangeBed(pageNumBed) {
      this.loadBed(pageNumBed)
    },
    disabled(status){  //是否禁用按钮
      if(status==='空'){
        return true;
      }
      // 将 tableData 中的 userName 提取到一个 Map 中
      const userNames = this.tableData.map(item => item.userName);
      if(userNames.length === 0 || !userNames.includes(status)){
        return true;
      }
      // 遍历 tableData 查找匹配的 userName
      for (const item of this.tableData) {
        if (item.userName === status) {
          if(item.traverse.careStatus === '已护理'){
            return true;
          }
        }
      }
      return false;
    }, 
    getBackgroundColor(wardName) {
      // 根据wardName返回不同的背景颜色
      switch (wardName) {
        case '重症监护室':
          return '#fff5f4';
        case '病房A':
          return '#fffff4';
        case '病房B':
          return '#f1fff3';
        case '病房C':
          return '#fef4ff';
        default:
          return '#ffffff';
      }
    }
  }
}
</script>


<style scoped>
.tables-container {
  display: flex;
  justify-content: space-between;
}

::v-deep .card {
  padding: 12px;
  margin-bottom: 10px;
}

.value {
  margin-bottom: 2px;
  font-size: 16px;
}
</style>