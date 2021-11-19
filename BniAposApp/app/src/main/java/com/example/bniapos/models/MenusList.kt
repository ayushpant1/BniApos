import com.google.gson.annotations.SerializedName

data class MenusList(
    @SerializedName("Menus") val menus: Menus
)

data class Menus(

    @SerializedName("MenuLink") val menuLink: List<MenuLink>
)

data class MenuLink(

    @SerializedName("Id") val id: Int,
    @SerializedName("ParentId") val parentId: Int,
    @SerializedName("DisplayText") val displayText: String,
    @SerializedName("IconName") val iconName: String,
    @SerializedName("SortOrder") val sortOrder: Int,
    @SerializedName("Type") val type: String,
    @SerializedName("LinkId") val linkId: Int,
    @SerializedName("ReceiptTemplate") val receiptTemplate: ReceiptTemplate,
    @SerializedName("FailedReceiptId") val failedReceiptId: Int
)

data class ReceiptTemplate(

    @SerializedName("Id") val id: Int
)