
function tableChannelClickRedirect() {
    $(".channel tr").click(function (e) {
        var link_td = $(this).children("td").first();
        document.location.href = $(("a"), link_td).attr("href");
        e.stopPropagation();
    });
}


function suggestUser() {
    $(".searchUser").select2({
        minimumInputLength: 2,
        ajax: {
            url: '/api/v1/users',
            dataType: 'json',
            type: "GET",
            quietMillis: 50,
            data: function (term) {
                return {
                    q: term
                };
            },
            results: function (data) {
                return {
                    results: $.map(data, function (item) {
                        return {
                            text: item.email,
                            slug: 'test1',
                            id: item.id
                        }
                    })
                };
            }
        },
        dropdownCssClass: "bigdrop"
    });
}
//$("#memberships_members").select2({
//    placeholder: "Search for a user",
//    minimumInputLength: 3,
//    ajax: {
//        url: "/api/v1/users",
//        dataType: 'jsonp',
//        quietMillis: 100,
//        data: function (term) { // page is the one-based page number tracked by Select2
//            return {
//                q: term //search term
//            };
//        },
//        results: function (data) {
//            return {results: data};
//        }
//    },
//    formatResult: function(data) {return data.id;}, // omitted for brevity, see the source of this page
//    formatSelection: function(data) { return data.email; }, // omitted for brevity, see the source of this page
//    escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
//});