package com.github.xiesen.junit.test;

import cn.hutool.core.codec.Base64;

/**
 * @author 谢森
 * @since 2021/3/25
 */
public class Base64Utils {
    public static final String PARSE_CONF = "ZmlsdGVyeyAKICBpZiAgKCAgW2NvbGxlY3RydWxlaWRdID09IDIgKSAgCiAKCnsKCiAgICBydWJ5ewogICAgICAgIGlkPT4iMl80NCIKICAgICAgICBjb2RlPT4iCiAgICAgICAgICAgIGV2ZW50SGFzaCA9IGV2ZW50LnRvX2hhc2gKICAgICAgICAgICAgZXZlbnQuc2V0KCdvZmZzZXQnLGhhbmRsZU9mZnNldChldmVudEhhc2gpKQogICAgICAgICAgICBldmVudC5zZXQoJ3NvdXJjZScsIGhhbmRsZVNvdXJjZShldmVudEhhc2gpKQogICAgICAgIAogICAgICAgICAgICBkaW1lbnNpb25zID0gSGFzaC5uZXcKICAgICAgICAgICAgZGltZW5zaW9ucyA9IHsKICAgICAgICAgICAgICAgICdob3N0bmFtZScgPT4gaGFuZGxlSG9zdE5hbWUoZXZlbnRIYXNoKSwKICAgICAgICAgICAgICAgICdhcHBzeXN0ZW0nID0+ICBldmVudC5nZXQoJ2FwcHN5c3RlbScpLAogICAgICAgICAgICAgICAgJ2FwcHByb2dyYW1uYW1lJyA9PiBldmVudC5nZXQoJ2FwcHByb2dyYW1uYW1lJyksCiAgICAgICAgICAgICAgICAnc2VydmljZW5hbWUnID0+IGV2ZW50LmdldCgnc2VydmljZW5hbWUnKSwKICAgICAgICAgICAgICAgICdzZXJ2aWNlY29kZScgPT4gZXZlbnQuZ2V0KCdzZXJ2aWNlY29kZScpLAogICAgICAgICAgICAgICAgJ2NsdXN0ZXJuYW1lJyA9PiBldmVudC5nZXQoJ2NsdXN0ZXJuYW1lJyksCiAgICAgICAgICAgICAgICAnaXAnID0+IGV2ZW50LmdldCgnaXAnKQogICAgICAgICAgICAgICAgfQogICAgICAgICAgICBldmVudC5zZXQoJ2RpbWVuc2lvbnMnLGRpbWVuc2lvbnMpCiAgICAgICAgICAgICNnZXQgbWVhc3VyZXMKICAgICAgICAgICAgbWVhc3VyZUhhc2ggPSBIYXNoLm5ldwogICAgICAgICAgICBtZWFzdXJlSGFzaCA9IHsKICAgICAgICAgICAgICAgICdjcHVfdXNlZCcgPT4gMS4wCiAgICAgICAgICAgICAgICAnZXJyb3JNZXRyaWMnID0+IG51bGwKICAgICAgICAgICAgfQogICAgICAgICAgICBldmVudC5zZXQoJ21lYXN1cmVzJywgbWVhc3VyZUhhc2gpCiAgICAgICAgICAgICNnZXQgbm9ybWFsRmllbGRzCiAgICAgICAgICAgIG90aGVyZmllbGRIYXNoID0gSGFzaC5uZXcKICAgICAgICAgICAgb3RoZXJmaWVsZEhhc2ggPSB7CiAgICAgICAgICAgICAgICAnY29sbGVjdHRpbWUnID0+IGV2ZW50LmdldCgnQHRpbWVzdGFtcCcpLnRvX3MsCiAgICAgICAgICAgICAgICAnbWVzc2FnZScgPT4gZXZlbnQuZ2V0KCdtZXNzYWdlJykKICAgICAgICAgICAgICAgIH0KCQkJb3RoZXJmaWVsZEhhc2hbJ2NvbGxlY3R0aW1lXzInXT0oVGltZS5uZXcoKS04KjYwKjYwKS5pc284NjAxKDMpLnRvX3MuZ3N1YignKzA4OjAwJywnWicpCgkJCW90aGVyZmllbGRIYXNoWydsb2dzdGFzaF9kZWFsX2lwJ109JyR7TE9HU1RBU0hfSFRUUF9IT1NUfScKCQkJb3RoZXJmaWVsZEhhc2hbJ2xvZ3N0YXNoX2RlYWxfbmFtZSddPScke0xPR1NUQVNIX0lOU1RBTkNFX05BTUV9JwoJCQlvdGhlcmZpZWxkSGFzaFsnc291cmNlJ10gPSBldmVudC5nZXQoJ3NvdXJjZScpCiAgICAgICAgICAgIGV2ZW50LnNldCgnbm9ybWFsRmllbGRzJywgb3RoZXJmaWVsZEhhc2gpCiAgICAgICAgICAgIGV2ZW50LnNldCgndGltZXN0YW1wJyxldmVudC5nZXQoJ0B0aW1lc3RhbXAnKSkKICAgICAgICAgICAgCiAgICAgICAgICAgICIKICAgICAgICBpbml0PT4iCiAgICAgICAgICAgIGRlZiBoYW5kbGVPZmZzZXQoZXZlbnRIYXNoKQogICAgICAgICAgICAgICAgb2Zmc2V0ID0gMAogICAgICAgICAgICAgICAgb2Zmc2V0ID0gZ2V0SGFzaFZhbHVlKGV2ZW50SGFzaCwgJ29mZnNldCcsIG9mZnNldCkKICAgICAgICAgICAgICAgIGlmIG9mZnNldCA9PSAwCiAgICAgICAgICAgICAgICAgICBsb2cgPSBnZXRIYXNoVmFsdWUoZXZlbnRIYXNoLCAnbG9nJywgSGFzaC5uZXcpCiAgICAgICAgICAgICAgICAgICBsb2c9bG9nLnRvX2hhc2gKICAgICAgICAgICAgICAgICAgIG9mZnNldCA9IGdldEhhc2hWYWx1ZShsb2csICdvZmZzZXQnLCBvZmZzZXQpCiAgICAgICAgICAgICAgICBlbmQKICAgICAgICAgICAgICAgIHJldHVybiBvZmZzZXQKICAgICAgICAgICAgZW5kCiAgICAgICAgICAgIAogICAgICAgICAgICBkZWYgaGFuZGxlU291cmNlKGV2ZW50SGFzaCkKICAgICAgICAgICAgICAgIHNvdXJjZSA9ICcnCiAgICAgICAgICAgICAgICBzb3VyY2UgPSBnZXRIYXNoVmFsdWUoZXZlbnRIYXNoLCAnc291cmNlJywgc291cmNlKQogICAgICAgICAgICAgICAgaWYgc291cmNlID09ICcnCiAgICAgICAgICAgICAgICAgICBsb2cgPSBnZXRIYXNoVmFsdWUoZXZlbnRIYXNoLCAnbG9nJywgSGFzaC5uZXcpCiAgICAgICAgICAgICAgICAgICBsb2c9bG9nLnRvX2hhc2gKICAgICAgICAgICAgICAgICAgIGZpbGUgPSBnZXRIYXNoVmFsdWUobG9nLCAnZmlsZScsIEhhc2gubmV3KQogICAgICAgICAgICAgICAgICAgZmlsZSA9IGZpbGUudG9faGFzaAogICAgICAgICAgICAgICAgICAgc291cmNlID0gZ2V0SGFzaFZhbHVlKGZpbGUsICdwYXRoJywgc291cmNlKQogICAgICAgICAgICAgICAgZW5kCiAgICAgICAgICAgICAgICByZXR1cm4gc291cmNlCiAgICAgICAgICAgIGVuZAogICAgICAgICAgICAKICAgICAgICAgICAgZGVmIGhhbmRsZUhvc3ROYW1lKGV2ZW50SGFzaCkKICAgICAgICAgICAgICAgIGhvc3RuYW1lID0gJ2VtcHR5SG9zdCcKICAgICAgICAgICAgICAgIGlmIGV2ZW50SGFzaC5oYXNfa2V5PygnYmVhdCcpCiAgICAgICAgICAgICAgICAgICBiZWF0ID0gZ2V0SGFzaFZhbHVlKGV2ZW50SGFzaCwgJ2JlYXQnLCBIYXNoLm5ldykKICAgICAgICAgICAgICAgICAgIGJlYXQ9YmVhdC50b19oYXNoCiAgICAgICAgICAgICAgICAgICBob3N0bmFtZSA9IGdldEhhc2hWYWx1ZShiZWF0LCAnaG9zdG5hbWUnLCBob3N0bmFtZSkKICAgICAgICAgICAgICAgIGVuZAogICAgICAgICAgICAgICAgaWYgaG9zdG5hbWUgPT0gJ2VtcHR5SG9zdCcgYW5kIGV2ZW50SGFzaC5oYXNfa2V5PygnaG9zdCcpCiAgICAgICAgICAgICAgICAgICBob3N0ID0gZ2V0SGFzaFZhbHVlKGV2ZW50SGFzaCwgJ2hvc3QnLCBIYXNoLm5ldykKICAgICAgICAgICAgICAgICAgIGhvc3Q9aG9zdC50b19oYXNoCiAgICAgICAgICAgICAgICAgICBob3N0bmFtZSA9IGdldEhhc2hWYWx1ZShob3N0LCAnbmFtZScsIGhvc3RuYW1lKQogICAgICAgICAgICAgICAgZW5kCiAgICAgICAgICAgICAgICByZXR1cm4gaG9zdG5hbWUKICAgICAgICAgICAgZW5kCiAgICAgICAgICAgIAogICAgICAgICAgICAKICAgICAgICAgICAgZGVmIGdldEhhc2hWYWx1ZSh0YXJnZXRIYXNoLCBrZXksIGRlZmF1bHRWYWx1ZSkKICAgICAgICAgICAgICAgIHZhbHVlID0gZGVmYXVsdFZhbHVlCiAgICAgICAgICAgICAgICBpZiB0YXJnZXRIYXNoLmhhc19rZXk/a2V5CiAgICAgICAgICAgICAgICAgICB2YWx1ZSA9IHRhcmdldEhhc2guZmV0Y2goa2V5LCBkZWZhdWx0VmFsdWUpCiAgICAgICAgICAgICAgICBlbmQgICAKICAgICAgICAgICAgICAgIHJldHVybiB2YWx1ZQogICAgICAgICAgICBlbmQKICAgICAgICAKICAgICAgICAiCiAgICB9Cgp9CiAKCiAgICBpZiAhKFtkaW1lbnNpb25zXSkge2Ryb3B7fX0KCgp9Cg==";

    public static void main(String[] args) {
        System.out.println(Base64.encode(PARSE_CONF));
        System.out.println(Base64.decodeStr(PARSE_CONF));
    }
}
