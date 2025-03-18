<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入病房名称查询" style="width: 200px" v-model="wardName"></el-input>
      <el-button type="primary" plain style="margin-left: 10px" @click="loadBed(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="success" plain style="margin-left: 10px" @click="goToWards">病房信息</el-button>
    </div>

    <div class="tables-container" style="display: flex; justify-content: space-between; width: 100%;">
      <div class="table" style="width: 40%; margin-right: 10px;">
        <el-row :gutter="24">
          <el-col :span="8" v-for="item in BedData" :key="item.id">
            <div :style="{ textAlign: 'center', backgroundColor: getBackgroundColor(item.wardName) }" class="card">
              <div class="value">{{ item.wardName }}</div>
              <div class="value">{{ item.bedName }}</div>
              <div class="value">{{ item.status }}</div>
              <div style="margin-top: 15px">
                <el-button type="primary" size="mini" :disabled="item.status === '空'"
                  @click="confirmLeave(item)">出院</el-button>
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
        <el-table :data="tableData" strip>
          <!-- <el-table-column prop="id" label="序号" align="center" sortable></el-table-column> -->
          <el-table-column prop="userName" label="姓名" align="center" width="80px"></el-table-column>
          <el-table-column prop="doctorName" label="主治医生" show-overflow-tooltip align="center"
            width="80px"></el-table-column>
          <el-table-column prop="traverse.mainDiagnosis" label="主要诊断" show-overflow-tooltip
            align="center"></el-table-column>
          <!--<el-table-column prop="wardName" label="病房号" show-overflow-tooltip align="center"></el-table-column> -->
          <el-table-column label="操作" align="center" width="110px">
            <template v-slot="scope">
              <el-button type="primary" plain @click="handleAssign(scope.row)"
                :disabled="scope.row.traverse.inHospital === '已住院'">分配病床</el-button>
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
    <!-- dialog -->
    <el-dialog title="分配病床" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px">
        <el-form-item label="病房名" prop="wardName">
          <el-select v-model="form.bedId" placeholder="请选择病床">
            <div v-for="item in wardList" :key="item.id">
              <el-option :label="item.wardName+'-'+item.bedName" :value="item.id"></el-option>
            </div>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import { MessageBox } from 'element-ui';

export default {
  name: "AssignBeds",
  data() {
    return {
      tableData: [],
      BedData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      pageNumBed: 1,
      pageSizeBed: 9,
      totalBed: 0,
      wardName: null,  //用于查询
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      wardList: [],
    };
  },
  created() {
    this.load(1); // 查询病例
    this.loadBed(1);
    this.findWards(); //查询所有的病房信息循环赋值给select下拉框
  },
  methods: {
    findWards() {
      request.get("/AssignBeds/selectBedAll").then(res => {
        if (res.code === "200") {
          this.wardListData = res.data;
          this.wardList = this.wardListData.filter(item => item.status === '').map(item => {
            return item;
          });
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get('/AssignBeds/selectTraversePage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        },
      }).then(res => {
        this.tableData = res.data?.list;
        this.total = res.data?.total;
      });
    },
    loadBed(pageNumBed) {
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
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleCurrentChangeBed(pageNumBed) {
      this.loadBed(pageNumBed)
    },
    reset() {
      this.wardName = null;
      this.loadBed(1);
    },
    handleAssign(row) {   // 新增数据（bed_id == null 的新增一个bed_id）
      this.form = JSON.parse(JSON.stringify(row));
      this.form.traverse.inHospital = '已住院';
      this.fromVisible = true;   // 打开弹窗
    },
    save() { // 分配病房
      this.form.traverse.bedId = this.form.bedId;
      request.put("/AssignBeds/save", this.form).then(res => {
        if (res.code === "200") {
          this.load(1);
          this.fromVisible = false;
        } else {
          this.$message.error(res.msg);
        }
      });
      let bedForm = {};
      bedForm.id = this.form.bedId;
      bedForm.status = this.form.userName;
      request.put("/AssignBeds/updateBedById", bedForm).then(res => {
        if (res.code === "200") {
          this.$message.success('保存成功');
          this.loadBed(1);
          this.findWards();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    confirmLeave(item) {
      const status = this.findTableRow(item.status);
      MessageBox.confirm(`确定要为${item.wardName}${item.bedName}${item.status}办理出院吗？`, '出院确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.leave(status);
      }).catch(() => {
        //提示用户操作已取消
        this.$message({
          type: 'info',
          message: '出院操作已取消'
        });
      });
    },
    findTableRow(userName) {
      return this.tableData.find(row => row.userName === userName);
    },
    leave(row) {  //出院
      this.form = JSON.parse(JSON.stringify(row));
      this.form.traverse.inHospital = '已出院';
      request.put("/AssignBeds/save", this.form).then(res => {
        if (res.code === "200") {
          this.load(1);
          this.loadBed(1);
        } else {
          this.$message.error(res.msg);
        }
      });
      let bedForm = {};
      bedForm.id = this.form.traverse.bedId;
      bedForm.status = '';
      request.put("/AssignBeds/updateBedById", bedForm).then(res => {
        if (res.code === "200") {
          this.$message.success("出院成功");
          this.loadBed(1);
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    goToWards() {
      this.$router.push({ name: "Ward" })
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
    },
  }
};
</script>

<style scoped>
.tables-container {
  display: flex;
  justify-content: space-between;
}

/* .table {
  flex: 1;
} */

::v-deep .el-input__inner {
  font-family: "SimSun", "宋体", serif;
  font-size: 16px;
  color: blue !important;
}

::v-deep .el-dialog {
  position: relative;
  margin: 250px 650px 0px !important;
  background: #fff;
  border-radius: 20px;
  box-shadow: inset 0 1px 10px rgba(0, 0, 0, .3);
  box-shadow: outset 0 1px 10px rgba(0, 8, 255, 0.3);
  box-sizing: border-box;
  width: 40%;
}

.el-dialog__wrapper {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    margin: 0;
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