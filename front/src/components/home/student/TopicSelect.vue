<template>
    <div class="topicSelectMain">
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <div class="card_header_item">你选择的课题是：{{ selectedTopic }}</div>
                <div>
                    <el-statistic @finish="selectFinish" :value="deadline" time-indices prefix="距离本次抢题任务结束还有：">
                    </el-statistic>
                </div>

            </div>
            <div class="text item">
                <el-table stripe :data="topicList" max-height="650">
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column prop="name" label="课题名称" min-width="400">
                    </el-table-column>
                    <el-table-column prop="type" label="课题类型" min-width="100">
                    </el-table-column>
                    <el-table-column prop="source" label="课题来源" min-width="100">
                    </el-table-column>

                    <el-table-column label="操作" min-width="200">
                        <template slot-scope="scope">
                            <el-button v-if="selectedTopic===''" plain type="primary" @click="selectTopic(scope.row)">选择课题</el-button>
                            <el-button v-else plain type="warning" @click="resetTopic(scope.row)">重新选择</el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <!-- 分页 -->
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-sizes="pageSizeList" :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="total">
                </el-pagination>

            </div>
        </el-card>


    </div>
</template>
<script>
export default {
    name: "TopicSelect",
    data() {
        return {
            topicList: [],
            selectedTopic: '',
            deadline: '',
            // 分页
            total: '',
            currentPage: '',
            pageSize: '',
            pageSizeList: []
        }
    },
    methods: {

        // 分页的pageSize
        handleSizeChange(val) {
            this.pageSize = val
        },
        // 分页的currentPage
        handleCurrentChange(val) {
            this.currentPage = val
        },
        //选择课题按钮
        selectTopic(row) {
            this.$confirm('确认选择这个毕设课题: ' + row.name + ' 吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {

                //发送请求 修改毕设课题的状态

                this.selectedTopic = row.name

                this.$message({
                    type: 'success',
                    message: '成功选择课题!'
                });


            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消选择'
                });
            });
        },
        //重新选择按钮
        resetTopic(row){
            this.$confirm('确定把你的毕设课题更改为: ' + row.name + ' 吗?', '提示', {
                confirmButtonText: '确认更改',
                cancelButtonText: '取消更改',
                type: 'warning'
            }).then(() => {

                //发送请求 修改旧毕设课题的状态
                
                //发送请求 修改毕设课题的状态
                this.selectedTopic = row.name

                this.$message({
                    type: 'success',
                    message: '成功更改课题!'
                });


            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消选择'
                });
            });
        },
        //课题选择结束
        selectFinish() {
            this.$confirm('选题任务已经结束！', '提示', {
                confirmButtonText: '确定',
                type: 'warning'
            }).then(() => {
                this.$router.push("/home/student/studentTopic")
            })
        }
    },
    created() {
        //倒计时
        let date = new Date(parseInt(this.$route.query.deadline))
        this.deadline = date.getTime()

        //分页
        this.total = 100
        this.currentPage = 1
        this.pageSizeList = [5, 10, 15, 20]
        this.pageSize = this.pageSizeList[0]

        //根据毕业生用户的专业获取 毕设课题列表 
        this.topicList = []

        for (let index = 0; index < 100; index++) {
            this.topicList.push(
                {
                    name: "基于xxx的xxx系统",
                    type: "设计",
                    source: "自命题"
                }
            )
        }
    },
    beforeUpdate(){
        //更新毕设课题列表 
        this.topicList=[]
        for (let index = 0; index < 10; index++) {
            this.topicList.push(
                {
                    name: "基于xxx的xxx系统",
                    type: "设计",
                    source: "自命题"
                }
            )
        }
    }

}
</script>
<style scoped lang="less">
.card_header {
    font-size: 24px;
}

.card_header_item {
    margin-bottom: 20px;

}

/deep/.el-statistic {
    font-size: 18px;
}

.box-card {
    height: 818px;
    line-height: 18px;
    background-color: rgba(255, 255, 255, 0.9);
}

/deep/.el-table {
    font-size: 16px;
    color: #606266;
    background-color: rgba(255, 255, 255, 0);
}

/deep/.el-table th.el-table__cell {
    overflow: hidden;
    -webkit-user-select: none;
    -moz-user-select: none;
    user-select: none;
    background-color: #FFF;
    line-height: 40px;
    font-size: 18px;
    color: black;
}

.topicSelectMain {
    background-color: rgba(255, 255, 255, 0);
}
</style>