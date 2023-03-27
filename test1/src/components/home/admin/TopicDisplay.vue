<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <span>课题信息</span>
            </div>
            <div class="item">
                <el-table height="712" empty-text="暂无数据" :data="topicList" scripe border style="width: 100%">
                    <el-table-column type="index" :index="indexInit" fixed>
                    </el-table-column>
                    <el-table-column prop="topicName" label="课题名称" fixed min-width="350px">
                    </el-table-column>
                    <el-table-column prop="createdName" label="导师名称" min-width="120px">
                    </el-table-column>
                    <el-table-column :filters="filterMajor" :filter-multiple="false" filter-placement="bottom-start"
                        :filter-method="filterMethodMajor" prop="dictMajor.description" label="专业" min-width="200px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicType" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicType" prop="dictTopicType.description" label="课题类别"
                        min-width="110px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicSource" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicSource" prop="dictTopicSource.description" label="课题来源"
                        min-width="110px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicPassed" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicPassed" prop="dictTopicPassed.description" label="审核结果"
                        min-width="110px">
                    </el-table-column>
                    <el-table-column prop="checkSuggestions" label="修改建议" min-width="190px" width="190px">
                    </el-table-column>
                    <el-table-column prop="checkedName" label="审核员名称" min-width="120px">
                    </el-table-column>

                    <el-table-column prop="createdTime" label="提交时间" min-width="200px">
                    </el-table-column>
                    <el-table-column prop="checkedTime" label="审核时间" min-width="200px">
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
import jwt_prase from '@/assets/js/jwtPrase'

export default {
    name: "TopicDisplay",
    methods: {
        // 分页的pageSize
        handleSizeChange(val) {
            this.pageSize = val
        },
        // 分页的currentPage
        handleCurrentChange(val) {
            this.currentPage = val
        },
        indexInit(index) {
            return index + (this.currentPage - 1) * this.pageSize + 1
        },
        filterMethodTopicType(value, row) {
            return row.dictTopicType.description === value
        },
        filterMethodTopicSource(value, row) {
            return row.dictTopicSource.description === value
        },
        filterMethodTopicPassed(value, row) {
            return row.dictTopicPassed.description === value
        },
        filterMethodMajor(value, row) {
            return row.dictMajor.description === value
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

        //根据college 毕设课题列表
        async getTopicList() {
            let college = jwt_prase().college
            let year = localStorage.getItem("year")
            const topicResp = await this.$http.get("/topic/" + this.currentPage + "/" + this.pageSize + "/" + college + "/" + year)

            if (topicResp.data.code === 200) {
                console.log(topicResp.data);
                //分页
                this.total = topicResp.data.data.total

                this.topicList = topicResp.data.data.records
                this.praseTopicList(this.topicList)
            }
        },

        //数据处理
        async praseTopicList(topiclist) {
            const _this = this
            topiclist.forEach((topic, index, arr) => {

                //处理时间数据
                topic.createdTime = new Date(topic.createdTime).toLocaleString()

                //处理审核状态
                if (topic.checked === 1) {
                    //未审核
                    topic.dictTopicPassed.description = "未审核"
                } else {
                    //解析审核时间
                    topic.checkedTime = new Date(topic.checkedTime).toLocaleString()
                }

                topiclist = arr
            });
        }
    },
    data() {
        return {
            filterTopicPassed: [
                {
                    text: "不及格",
                    value: "不及格"
                },
                {
                    text: "基本及格",
                    value: "基本及格"
                },
                {
                    text: "及格",
                    value: "及格"
                },
                {
                    text: "未审核",
                    value: "未审核"
                }
            ],
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
            topicList: [],

            // 分页
            total: 100,
            currentPage: 1,
            pageSize: 10,
            pageSizeList: [5, 10, 15, 20]

        }

    },
    async created() {
        //表格初始化
        await this.initTable()

        //发送请求获取topicList
        this.getTopicList()
    },
    watch: {
        pageSize() {
            this.getTopicList()
        },
        currentPage() {
            this.getTopicList()
        }
    }
}
</script>

<style scoped lang="less">
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