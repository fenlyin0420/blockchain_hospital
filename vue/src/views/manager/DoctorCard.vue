<template xmlns="">
  <div>
    <!-- 搜索区 -->
    <div class="search">
      <!-- 选择医院 -->
      <label for="hospital">医院:</label>
      <el-select id="hospital" placeholder="请选择医院" v-model="hospitalId" @change="query()">
        <el-option
          v-for="item in hospitalList"
          :key="item.id"
          :label="item.hospitalName"
          :value="item.id"
        ></el-option>
      </el-select>

      <!-- 选择科室 -->
      <label for="departmentSelect">科室:</label>
      <el-select id="departmentSelect" placeholder="请选择科室" v-model="departmentId" @change="query()">
        <el-option
          v-for="item in departmentList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        ></el-option>
      </el-select>

      <!-- 选择日期 -->
      <label for="selectedDate">日期:</label>
      <el-select id="selectedDate" placeholder="请选择日期" v-model="selectedDate" @change="query()">
        <el-option
          v-for="item in timestamp"
          :key="item.id"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>

    </div>

    <!-- doctorCard 显示区域 -->
    <div class="table" style="padding: 15px 20px">
      <el-row :gutter="24">
        <el-col :span="8" v-for="item in tableData" :key="item.id" style="margin-bottom: 20px">
          <div style="text-align: center; background-color: #ecf8fd" class="card">
            <img
              :src="item.avatar"
              alt=""
              style="width: 100px; height: 100px; border-radius: 50%"
            />
            <!-- <div style="font-weight: 550; margin-top: 10px">
              医院
              <span style="color: #383535; margin-left: 5px; font-weight: 500">{{
                item.hospitalName
              }}</span>
            </div> -->
            <div style="font-weight: 550; margin-top: 10px">
              {{ item.name }}
              <span style="color: #383535; margin-left: 5px; font-weight: 500">{{
                item.departmentName
              }}</span>
            </div>
            <div
              style="
                margin-top: 20px;
                color: #353523;
                padding: 0 10px;
                text-align: left;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 4;
              "
            >
              简介：{{ item.description }}
            </div>
            <div style="margin-top: 15px">
              挂号费：<span style="color: red; font-weight: 550; margin-right: 20px"
                >￥{{ item.price }}</span
              >
              剩余：{{ item.num }}
            </div>
            <!-- 挂号按钮 -->
            <div style="margin-top: 15px">
              <el-button
                type="primary"
                size="mini"
                :disabled="disabled"
                @click="showDialog(item)"
                >挂号</el-button>
            </div>

            <el-dialog title="确认订单" :visible.sync="dialogVisible" width="30%">
              <Payment :form="dialogDate"></Payment>

              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="reserve(clickedItem)">确定</el-button>
              </span>
            </el-dialog>
          </div>
        </el-col>
      </el-row>

      <!-- 选择分页区 -->
      <div class="pagination">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import Payment from "@/views/manager/Payment.vue";
export default {
  components: {
    Payment,
  },
  name: "Doctor",
  data() {
    return {
      /** API: /doctor/selectpage2 */
      tableData: [],
      /** API: /plan/selectAll */
      planList: [],
      /** API: /hospital/selectAll */
      hospitalList: [],
      /** API: /department/selectAll */
      departmentList: [],
      pageNum: 1, // 当前的页码
      pageSize: 10, // 每页显示的个数
      total: 0,
      /** 用户选择的医院 */
      hospitalId: null,
      /** 用户选择的科室 */
      departmentId: null,
      /** 用户选择的日期 */
      selectedDate: null,
      fromVisible: false,
      form: {},
      /** user entity, an object */
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      rules: {},
      ids: [],
      disabled: false,
      /** 顶部搜索区域 tab 默认项*/
      activeName: "hospital",
      /** 支付界面可视性 */
      dialogVisible: false,
      /** 向支付界面传递的数据 */
      dialogDate: {},
      /** 日期数据
       * {
       *   id: number,
       *   label: string,
       *   value: string
       * }
       */
      timestamp: [],
      clickedItem: null
    };
  },
  created() {
    /** 生成七天日期数据 */
    const weekIndex = [
      "星期日",
      "星期一",
      "星期二",
      "星期三",
      "星期四",
      "星期五",
      "星期六",
    ];
    let startDate = new Date();
    let dateTemp = new Date();
    for (let i = 0; i < 7; i++) {
      dateTemp.setDate(startDate.getDate() + i);
      let week = dateTemp.getDay();
      let label = weekIndex[week] + "🔹️" + dateTemp.toISOString().split("T")[0];
      this.timestamp.push({ id: i, label, value: dateTemp.toISOString().split("T")[0] });
    }
    Promise.all([this.loadDepartments(), this.loadHospitals()])
      .then(() => {
        this.query(1);
      })
      .catch((err) => {
        this.$message.error(err);
      });

    // 初始化过滤日期
    this.selectedDate = this.timestamp[0]?.value;
  },
  methods: {
    /**
     * 挂号操作
     * 需要修改的表：record.* plan.num user.account
     * @param item 医生信息
     */
    reserve(item) {
      this.dialogVisible = false;
      if (this.user.role !== "USER") {
        this.$message.warning("您的角色不支持挂号操作");
        return;
      }
      if (this.user.account < item.price) {
        this.$message.error("您的账余额不足");
        return -1;
      }
      let reserveBody = {
        userId: this.user.id,
        doctorId: item.id,
        hospitalId: item.hospitalId,
        time: item.selectedDate,
      };
      let planBody = {
        hospitalId: item.hospitalId,
        doctorId: item.id,
        date: item.selectedDate,
      };

      /** 修改挂号记录表 reserve */
      this.$request.post("/reserve/add", reserveBody).then((res) => {
        if (res.code !== "200") {
          this.$message.error(res.msg);
          return -1;
        }
      });
      /** 修改医生排班表中的剩余挂号数量 plan */
      this.$request.post("/plan/updateNum", planBody).then((res) => {
        if (res.code !== "200") {
          this.$message.error("update num failed");
          return -1;
        }
      });
      /** 修改账户余额 user */
      this.$request
        .put("/user/update", {
          id: this.user.id,
          account: this.user.account - item.price,
        })
        .then((res) => {
          if (res.code === "200") {
            this.$message.success("挂号成功");
            this.query(1);
          } else {
            this.$message.error(res.data);
          }
        });
    },
    showDialog(item) {
      this.clickedItem = item;
      this.dialogVisible = true;
      this.dialogDate = {
        department: item.departmentName,
        doctor: item.name,
        price: item.price,
        date: item.selectedDate,
      };
    },
    /**
     * 获取所有的医院
     */
    loadHospitals() {
      return new Promise((resolve, reject) => {
        this.$request
          .get("/hospital/selectAll")
          .then((res) => {
            if (res.code === "200") {
              this.hospitalList = res.data;
              // 初始化医院
              this.hospitalId = this.hospitalList[0]?.id;
              resolve();
            } else {
              this.$message.error(res.msg);
              reject();
            }
          })
          .catch((err) => {
            this.$message.error("加载医院数据失败");
            reject(err);
          });
      });
    },
    /**
     * 获取所有科室
     */
    loadDepartments() {
      return new Promise((resolve, reject) => {
        this.$request
          .get("/department/selectAll")
          .then((res) => {
            if (res.code === "200") {
              this.departmentList = res.data;
              // 初始化科室
              this.departmentId = this.departmentList[0]?.id;
              resolve();
            } else {
              this.$message.error(res.msg);
              reject();
            }
          })
          .catch((err) => {
            this.$message.error("加载科室信息失败");
            reject(err);
          });
      });
    },
    /**
     * 查询指定页的数据，并将返回数据保存在组件数据中
     * 初始化时默认查询第一页数据
     * res.data 中的 total 是返回的总记录数，被组件属性 total 引用
     * res.data 中的 list 是包含医生个人信息的对象，被组件属性 tableData 引用
     * @param pageNum 页号
     */
    query(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request
        .get("/doctor/selectPage2", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            departmentId: this.departmentId,
            hospitalId: this.hospitalId,
            selectedDate: this.selectedDate,
          },
        })
        .then((res) => {
          if (res.code === "200") {
            this.tableData = res.data?.list;
            this.total = res.data?.total;
          } else {
            this.$message.error(res.msg);
          }
        });
    },
    reset() {
      this.departmentId = this.departmentList[0]?.id;
      this.hospitalId = this.hospitalList[0]?.id;
      this.selectedDate = this.timestamp[0]?.value;
      this.query(1);
    },
    handleCurrentChange(pageNum) {
      this.query(pageNum);
    },
  },
};
</script>

<style scoped>
.table {
  font-size: 16px;
}

.searchBtn {
  margin: 5px 15px;
}

::v-deep .el-select {
  margin: 0 15px;
}

</style>
