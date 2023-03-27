<template>
    <div>
        <el-table max-height="540px" :data="topicList" stripe style="width: 100%">
            <el-table-column type="index">
            </el-table-column>
            <el-table-column prop="topicName" label="课题名称" min-width="400">
            </el-table-column>
            <el-table-column prop="dictTopicType.description" label="课题类型" min-width="180">
            </el-table-column>
            <el-table-column prop="dictTopicSource.description" label="课题来源">
            </el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
            :page-sizes="pageSizeList" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
    </div>

</template>

<script>
import jwt_prase from '@/assets/js/jwtPrase'

export default {
    name: "TopicPreview",
    data() {
        return {
            topicList: [],
            //分页
            total: 100,
            currentPage: 1,
            pageSize: 10,
            pageSizeList: [5, 10, 20, 30]
        }
    },
    methods: {
        handleSizeChange(val) {
            this.pageSize = val
        },
        handleCurrentChange(val) {
            this.currentPage = val
        },
        //根据 毕业生用户的专业 毕业年份  获取 毕设课题列表
        async getTopicList() {
            const resp = await this.$http.get("/topic/checked/" + this.currentPage + "/" + this.pageSize + "/" + jwt_prase().major + "/" + jwt_prase().year)
            if (resp.data.code === 200) {
                this.total = resp.data.data.total
                this.topicList = resp.data.data.records
            }
        }
    },
    created() {
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
/deep/.el-table {
    font-size: 18px;
    color: black;
}
</style>