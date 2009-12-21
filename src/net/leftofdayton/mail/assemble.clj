
(def *html-doctype "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">")

; build *html, *plain, *other, *header
(defn plain-msg-ids []
  (str "Message # " *msg-id " posted by " (substr *posted-by 1 \@)
          " from server " (substr *posted-by \@) ".\r\n\r\n"
          ) )

(defn plain-blurb []
   (str (plain-msg-ids)
"With " *news-server " you can control who sends you what news from where.\r\n"
"For details visit " *website ".\r\n"
) )

(defn content-type-multipart []
   (str "Content-Type: multipart/alternative;\r\n"
        "    boundary=\"" *boundary \" "\r\n\r\n"
        ) )

(def *mime-blurb (str "\r\n\r\nThis is a multi-part message in MIME format.\r\n\r\n"))

(defn html-blurb []
   (str "<br/><small>------------- "
"<p class='lod'>" (plain-msg-ids) "</p>\r\n"
"<p class='lod'>With " *news-server " you can control who sends you what news from where.\r\n"
"<a href='" *website "'>Click here for details.</a>\r\n"
"</p>"
   ) )






(defn assemble []
   (brk 'undefined)
   )
