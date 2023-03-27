<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <span>尚未审核的课题</span>
            </div>
            <div class="item">
                <el-table height="712" empty-text="暂无数据" :data="topicList" scripe border style="width: 100%">
                    <el-table-column fixed type="index" prop="id" :index="indexInit">
                    </el-table-column>
                    <el-table-column fixed prop="topicName" label="课题名称" min-width="350px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicType" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicType" prop="dictTopicType.description" label="课题类别"
                        min-width="120px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicSource" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicSource" prop="dictTopicSource.description" label="课题来源"
                        min-width="120px">
                    </el-table-column>
                    <el-table-column prop="createdName" label="提交人名称" min-width="120px">
                    </el-table-column>
                    <el-table-column :filters="filterMajor" :filter-multiple="false" filter-placement="bottom-start"
                        :filter-method="filterMethodMajor" prop="dictMajor.description" label="专业名称" min-width="120px">
                    </el-table-column>
                    <el-table-column prop="createdTime" label="提交时间" min-width="200px" width="200px">
                    </el-table-column>
                    <el-table-column min-width="100px">
                        <template slot-scope="scope">
                            <el-button class="btn_check" plain type="success" @click="showCheckFormDialog(scope)">审核
                            </el-button>
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
        <el-dialog title="课题审核详情页" :visible.sync="checkFormDialogVisible" width="600px">
            <div>
                <el-form label-position="top" :model="checkForm" :rules="rules" ref="checkForm">
                    <el-form-item label="课题名称">
                        <el-input disabled v-model="checkForm.topicName"></el-input>
                    </el-form-item>
                    <el-form-item label="是否及格" prop="passed">
                        <el-select v-model="checkForm.passed" placeholder="是否及格">
                            <el-option v-for="item in passedList" :label="item.description" :value="item.passed"
                                :key="item.passed"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="修改建议" prop="checkSuggestions">
                        <el-input type="textarea" v-model="checkForm.checkSuggestions"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-row class="btn_checkForm_div">
                            <el-button plain type="success" @click="checkSubmit">确认审核</el-button>
                            <el-button plain type="warning" @click="nextTopic">下一个课题</el-button>
                            <el-button plain type="danger" @click="closeCheckForm">关闭页面</el-button>
                        </el-row>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>

    </div>
</template>
<script>
import jwt_prase from '@/assets/js/jwtPrase';
export default {
    name: "TopicCheck",
    methods: {
        //下一个课题按钮
        nextTopic() {
            this.resetForm()
            if (this.tableIndex < this.topicList.length - 1) {
                this.tableIndex++
            }else{
                this.$message({
                message: "已经是最后一个课题：",
                type: 'error'
            })
            }
            this.checkForm.topicName = this.topicList[this.tableIndex].topicName
            this.checkForm.id = this.topicList[this.tableIndex].id

            this.$message({
                message: "已切换到课题：" + this.checkForm.topicName,
                type: 'success'
            })
        },

        async checkSubmit() {
            //提交课题审核
            this.$refs["checkForm"].validate((valid) => {
                if (valid) {
                    //验证通过
                    this.$confirm("确定提交课题：" + this.checkForm.topicName + " 的审核结果吗？", "审核课题", {
                        center: true,
                        cancelButtonText: "取消",
                        confirmButtonText: "提交",
                        type: "warning"
                    }).then(() => {
                        //发送请求，修改topic的审核结果
                        this.checkForm.checkedBy = jwt_prase().id
                        this.checkForm.checkedTime = new Date().toJSON()
                        this.checkForm.checkedName = jwt_prase().name

                        //请求
                        this.$http({
                            method: "put",
                            data: this.checkForm,
                            url: "/topic/check"
                        }).then(resp => {
                            console.log(resp);
                            if (resp.data.code === 200 && resp.data.data === true) {
                                this.$message({
                                    type: 'success',
                                    message: '审核成功!'
                                });
                            } else if (resp.data.code === 200 && resp.data.data === false) {
                                this.$message({
                                    type: 'error',
                                    message: '审核失败!请重试'
                                });
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: '系统错误！请稍后再试'
                                });
                            }
                        })


                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                    })
                } else {
                    return false;
                }
            });
        },
        //展示毕设课题审核页面
        showCheckFormDialog(scope) {
            this.resetForm()
            this.tableIndex = scope.$index

            let rowData = scope.row

            this.checkForm.topicName = rowData.topicName
            this.checkForm.id = rowData.id

            this.checkFormDialogVisible = true
        },
        //重置表单
        resetForm() {
            this.checkForm.id = ''
            this.checkForm.passed = ''
            this.checkForm.suggestions = ''
            this.checkForm.topicName = ''
        },

        //关闭审核页面
        closeCheckForm() {
            this.checkFormDialogVisible = false
        },
        // 分页的pageSize
        handleSizeChange(val) {
            this.pageSize = val
        },
        // 分页的currentPage
        handleCurrentChange(val) {
            this.currentPage = val
        },

        filterMethodTopicType(value, row) {
            return row.topicType === value
        },
        filterMethodTopicSource(value, row) {
            return row.topicSource === value
        },
        filterMethodMajor(value, row) {
            return row.dictMajor.description === value
        },
        //发送请求获取passedList
        async getpassedList() {
            const resp = await this.$http.get('/dict/topicPassed')
            if (resp.data.code === 200) {
                this.passedList = resp.data.data
            }
        },

        //根据college 毕设课题列表
        async getTopicList() {
            let college = jwt_prase().college
            let year = localStorage.getItem("year")
            const topicResp = await this.$http.get("/topic/unCheck/" + this.currentPage + "/" + this.pageSize + "/" + college + "/" + year)

            if (topicResp.data.code === 200) {
                //分页

                this.total = topicResp.data.data.total

                this.topicList = topicResp.data.data.records
                this.praseTopicList(this.topicList)
            }
        },

        indexInit(index) {
            return index + (this.currentPage - 1) * this.pageSize + 1
        },

        async initTable() {
            let college = jwt_prase().college
            const resp = await this.$http.get("/dict/major/college/" + college)

            const _this = this
            if (resp.data.code === 200 && resp.data.data !== null) {
                resp.data.data.forEach((major) => {
                    _this.filterMajor.push({
                        text: major.description,
                        value: major.description
                    })
                })
            }

        },

        //数据处理
        async praseTopicList(topiclist) {
            topiclist.forEach((topic, index, arr) => {
                //处理时间数据
                topic.createdTime = new Date(topic.createdTime).toLocaleString()

                topiclist = arr
            });
        }

    },
    data() {
        return {
            checkForm: {
                id: '',
                topicName: "",
                passed: '',
                checkSuggestions: "",
                checkedBy: '',
                checkedTime: '',
                checkedName: ''
            },
            passedList: [],
            topicList: [],
            checkFormDialogVisible: false,
            tableIndex: '',
            filterTopicSource: [
                {
                    text: "自命题",
                    value: "自命题"
                },
                {
                    text: "企业",
                    value: "企业"
                },
                {
                    text: "教师科研",
                    value: "教师科研"
                },
            ],
            filterTopicType: [
                {
                    text: "论文",
                    value: "论文"
                },
                {
                    text: "设计",
                    value: "设计"
                },
            ],
            filterMajor: [],
            rules: {
                checkSuggestions: [
                    { required: true, message: '请输入修改建议', trigger: 'blur' },
                ],
                passed: [
                    { required: true, message: '请选择审核结果', trigger: 'change' }
                ],
            },

            // 分页
            total: 100,
            currentPage: 1,
            pageSize: 10,
            pageSizeList: [5, 10, 15, 20]
        }

    },
    async created() {
        await this.initTable()
        //发送请求获取passedList
        await this.getpassedList()

        //发送请求获取topicList
        this.getTopicList()


    },
    watch: {
        pageSize() {
            this.getTopicList()
        },
        currentPage() {
            this.getTopicList()
        },
        checkFormDialogVisible(val) {
            if (val === false) {
                this.getTopicList()
            }
        }
    }
}
</script>

<style scoped lang="less">
.btn_checkForm_div {
    text-align: center;
}

/deep/.el-form-item {
    text-align: left;
    font-size: 18px;
    line-height: 23px;
}

/deep/.el-dialog__header {
    line-height: 23px;
}

/deep/.el-form-item__label {
    font-size: 18px;
}

.btn_check {
    width: 100px;
    font-size: 18px;
}

.box-card {
    height: 818px;
    line-height: 23px;
    background-color: rgba(255, 255, 255, 0.9);
}

.card_header {
    text-align: left;
    font-size: 20px;
}

.el-table {
    text-align: center;
    font-size: 18px;
}

/deep/.el-table__row {
    height: 66px;
}
</style>