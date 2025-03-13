<template>
    <div>
      <el-calendar v-model="currentDate">
        <template #dateCell="{ date, data }">
          <div :class="['date-cell', { 'has-schedule': getScheduleByDate(date).length > 0 }]">
            <span class="date">{{ data.day.split('-').slice(2).join('-') }}</span>
            <div v-for="item in getScheduleByDate(date)" :key="item.id" class="schedule-item">
              <el-tag :closable="false" color="success">
                <!-- <span>{{ item.doctorName }}</span> -->
                <span>{{ item.departmentName }}</span>
                <span>-就诊数{{ item.num }}人</span>
              </el-tag>
            </div>
          </div>
        </template>
      </el-calendar>
    </div>
  </template>
  
  <script>
  export default {
    name: "Plan",
    data() {
      return {
        tableData: [], // 所有的排班数据
        pageNum: 1, // 当前页码
        pageSize: 10, // 每页显示个数
        total: 0, // 总记录数
        user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
        currentDate: new Date(), // 日历当前显示的日期
      };
    },
    created() {
      this.load(1); // 组件创建时加载数据
    },
    methods: {
      /**
       * 加载指定日期的排班数据
       * @param pageNum 页号
       */
      load(pageNum) {
        if (pageNum) this.pageNum = pageNum;
        this.$request
          .get("/plan/selectPage", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              hospitalId: this.user.hospitalId,
            },
          })
          .then((res) => {
            this.tableData = res.data?.list || []; // 将排班数据存储到 tableData 中
            this.total = res.data?.total || 0; // 更新总记录数
          });
      },
      /**
       * 根据日期获取排班信息
       * @param date 日期
       * @returns {[]} 排班信息数组
       */
      getScheduleByDate(date) {
        const dateStr = this.formatDate(date);
        return this.tableData.filter(item => item.date === dateStr);
      },
      /**
       * 格式化日期为 YYYY-MM-DD 格式
       * @param date 日期对象
       * @returns {string} 格式化后的日期字符串
       */
      formatDate(date) {
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
      },
    },
  }
  </script>
  
  <style scoped>
  .date {
    display: block;
    margin-bottom: 5px;
    font-size: 14px;
    font-weight: bold;
  }
  .schedule-item {
    margin-bottom: 0px;
  }
  .el-tag {
    background-color: #e4edf7;
    border-color: #82d1f1;
    color: #36A7FF;
    display: inline-block;
    height: 32px;
    padding: 0 1px;
    line-height: 30px;
    font-size: 12px;
    border-width: 1px;
    border-style: solid;
    border-radius: 4px;
    box-sizing: border-box;
    white-space: nowrap;
  }
::v-deep thead {
    display: table-header-group;
    vertical-align: middle;
    unicode-bidi: isolate;
    border-color: inherit;
    background-color: #264387;
}
::v-deep .el-calendar-table thead th {
    padding: 12px 0;
    color: #ffffff;
    font-weight: 400;
}
::v-deep .el-calendar-table{
    table-layout: fixed;
    width: 100%;
    border-radius: 10px !important; 
    box-shadow : 0 4px 8px rgba(0, 0, 0, 0.1) !important; 
    background-color: #F8F8F8;
    /* background-image: url("@/assets/imgs/calendar5.jpg") !important; 设置背景图片 */
    /* background-size: cover; 背景图片覆盖整个表格 */
    /* background-position: center; 背景图片居中 */ 
}
::v-deep .el-calendar-table td.is-today {
    color: #409eff;
}
/* ::v-deep .el-calendar-table td.is-selected {
    background-color:transparent; /* 聚焦设置为透明 
}
::v-deep .el-calendar-table .el-calendar-day:hover {
    cursor: pointer;
    background-color: transparent;/* 鼠标聚焦也设置为透明 
} */
::v-deep.el-calendar__header {
    display: flex;
    justify-content: space-between;
    padding: 12px 20px;
    border-bottom: 1px solid #ebeef5;
}
::v-deep.el-calendar {
    background-color: #fff;
    border-radius: 10px !important; 
    box-shadow : 0 4px 8px rgba(0, 0, 0, 0.1) !important; 
}
  </style>