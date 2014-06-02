
function tableChannelClickRedirect() {
    $(".channel tr").click(function (e) {
        var link_td = $(this).children("td").first();
        document.location.href = $(("a"), link_td).attr("href");
        e.stopPropagation();
    });
}