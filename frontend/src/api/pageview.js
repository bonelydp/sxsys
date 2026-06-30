import request from "@/utils/request"

export const recordPageView = (type) => {
    return request.get("/pageview/record", { params: { type } })
}
