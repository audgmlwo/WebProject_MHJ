document.addEventListener("DOMContentLoaded", function () {
    const truncateElements = document.querySelectorAll("#marketing .\\33u .mbox-style .content ul li a"); // .3u를 .\\33u로 수정

    truncateElements.forEach(element => {
        const maxLength = 12; // 제한할 글자 수
        const originalText = element.textContent.trim(); // 텍스트 내용 가져오기

        if (originalText.length > maxLength) {
            element.textContent = originalText.slice(0, maxLength) + "..."; // 제한된 글자 수 뒤에 "..." 추가
        }
    });
});