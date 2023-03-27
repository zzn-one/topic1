<template>
    <div class="topicSelectMain">
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <div class="card_header_item">你当前已选择的课题是：{{ selectedTopic }}</div>
                <div>
                    <el-statistic @finish="selectFinish" :value="deadline" time-indices prefix="距离本次抢题任务结束还有：">
                    </el-statistic>
                </div>

            </div>
            <div class="text item">
                <el-table stripe :data="topicList" max-height="650" height="650">
                    <el-table-column type="index" :index="indexMethod">
                    </el-table-column>
                    <el-table-column prop="topicName" label="课题名称" min-width="400">
                    </el-table-column>
                    <el-table-column prop="dictTopicType.description" label="课题类型" min-width="100">
                    </el-table-column>
                    <el-table-column prop="dictTopicSource.description" label="课题来源" min-width="100">
                    </el-table-column>

                    <el-table-column label="操作" min-width="200">
                        <template slot-scope="scope">
                            <el-button v-if="selectedTopic === ''" plain type="primary"
                                @click="selectTopicBtn(scope.row)">选择课题</el-button>
                            <el-button v-else plain type="success" @click="resetTopicBtn(scope.row)">重新选择</el-button>
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
import jwtPrase from '@/assets/js/jwtPrase';
export default {
    name: "TopicSelect",
    data() {
        return {
            topicList: [],
            selectedTopic: '',
            deadline: '',
            // 分页
            total: 100,
            currentPage: 1,
            pageSize: 5,
            pageSizeList: [5, 10, 15, 20]

        }
    },
    methods: {
        //表的索引
        indexMethod(index) {
            return index + (this.currentPage - 1) * this.pageSize + 1
        },

        // 分页的pageSize
        handleSizeChange(val) {
            this.pageSize = val
        },
        // 分页的currentPage
        handleCurrentChange(val) {
            this.currentPage = val
        },
        //选择课题按钮
        selectTopicBtn(row) {
            this.$confirm('确认选择这个毕设课题: ' + row.topicName + ' 吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //发送请求 修改毕设课题的状态

                const formData = new FormData();

                let selected_by = jwtPrase().id
                let selected_name = jwtPrase().name

                formData.append("topicName", row.topicName);
                formData.append("selectedBy", selected_by);
                formData.append("selectedName", selected_name);

                this.selectTopic(formData)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消选择'
                });
            });
        },
        //提交选题申请
        async selectTopic(formData) {
            const resp = await this.$http.put("/topic/selectTopic", formData)

            if (resp.data.code === 200) {
                this.selectedTopic = resp.data.data

                this.$message({
                    type: 'success',
                    message: '成功选择课题!'
                });
            } else if (resp.data.code === 2001) {
                this.$message({
                    type: 'error',
                    message: resp.data.msg
                });
            } else if (resp.data.code === 2002) {
                this.$message({
                    type: 'error',
                    message: resp.data.msg
                });
            }

            this.getTopicList()

        },

        //重新选择按钮
        resetTopicBtn(row) {
            const _this = this
            this.$confirm('确定把你的毕设课题更改为: ' + row.topicName + ' 吗?', '提示', {
                confirmButtonText: '确认更改',
                cancelButtonText: '取消更改',
                type: 'warning'
            }).then(() => {
                //发送请求 修改旧毕设、毕设课题的状态
                const formData = new FormData();

                let selected_by = jwtPrase().id
                let selected_name = jwtPrase().name

                formData.append("oldTopic", _this.selectedTopic);
                formData.append("newTopic", row.topicName);
                formData.append("selectedBy", selected_by);
                formData.append("selectedName", selected_name);


                this.$http({
                    url: "/topic/resetTopic",
                    method: "put",
                    data: formData
                }).then((resp) => {

                    if (resp.data.code === 200) {
                        this.selectedTopic = resp.data.data

                        this.$message({
                            type: 'success',
                            message: '成功重选课题!'
                        });
                    } else if (resp.data.code === 2001) {
                        this.$message({
                            type: 'error',
                            message: resp.data.msg
                        });
                    } else if (resp.data.code === 2002) {
                        this.$message({
                            type: 'error',
                            message: resp.data.msg
                        });
                    }

                    this.getTopicList()
                })





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
        },

        //初始化selectedTopic
        async initSelectedTopic() {
            //查询当前用户是否已经有课题 根据sid
            let sid = jwtPrase().id
            const resp = await this.$http.get("/topic/selected/" + sid)

            if (resp.data.data !== null) {
                this.selectedTopic = resp.data.data.topicName
            }

        },


        //根据毕业生用户的专业获取 毕设课题列表 
        async getTopicList() {
            let major = jwtPrase().major
            let year = localStorage.getItem("year")

            const resp = await this.$http.get("/topic/checked/" + this.currentPage + "/" + this.pageSize + "/" + major + "/" + year)

            console.log(resp.data);
            if (resp.data.data !== null) {
                this.topicList = resp.data.data.records
                this.total = resp.data.data.total
            }
        }

    },
    watch: {
        currentPage() {
            this.getTopicList()
        },
        pageSize() {
            this.getTopicList()
        }
    },
    created() {
        //倒计时
        let date = new Date(parseInt(this.$route.query.deadline))
        this.deadline = date.getTime()



        this.initSelectedTopic()


        //根据毕业生用户的专业获取 毕设课题列表 
        this.getTopicList()

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