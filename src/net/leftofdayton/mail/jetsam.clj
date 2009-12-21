
(defn get-recdl [l]
     (if-let [ a (ss *m$ "ved: from" *i) ]
       (let [ a (ss *m$ \[ a)
              b (ss *m$ \] a)
              a (substr *m$ (inc a) b)
              ]
            (def *i b)
            (get-recdl (concat l (list a)))
            )
       l
       )
     )

(defn get-posted-by []
  (let [ a (ss *m$ "From: " *i)
         a (ss *m$ \< a)
         b (ss *m$ \> a) ]
       (def *i b)
       (substr *m$ (inc a) b)
       )
  )

 (defn get-ctol [m]
   (if-let [ a (ss *m$ "Content-Type: " *i)]
     (let [ $ (substr *m$ a (+ a 50))
            ]
          (def *i (+ a 20))
          (get-ctol
            (concat m (list (list
              (cond (ss $ "multi") "multi"
                    (ss $ "plain") "plain"
                    (ss $ "html")  "html"
                    true "other"
                    )
              a
              ) ) )
            )
         )
      m   
      ) ;end if-let
    )

(defn gen-newsid []
   (now)
   )

(defn get-subj []
   (def *i 0)
   (let [ a (ss *m$ "Subject: ")
          a (ss *m$ " " a)
          b (ss *m$ "\r" a)
          ]
        (substr *m$ (inc a) b)
        )
    )

(defn get-remove-content-data []
  (let [ a (ss *header "Content-Type:")
         b (ss *header "Content-Transfer" a)

         b (ss *header "\r\n" b)
         cdata$ (substr *header a b)
         ]
       (def *header (str (substr *header 0 a)
                         (substr *header (inc b))
                         ) )
       cdata$
       )
  )

(defn html-quoted [plain$]
  )


(def at$ "@")

(defn set-multi-plain []
  (let [
         a (ss+ *m$ "\r\n\r\n")
         mbody (substr *m$ a)
         x (def *header (substr *m$ 0 a))
         atp (ss "asdf" at$)
         plain 
           (str *boundary "\r\n"
                (get-remove-content-data) ; plain from *header
                "\r\n\r\n"
                "\r\n"
                *lp*
                "Newsid=" *newsid *nl*
                " Posted by " (substr *posted-by 0 5)
                   " at email server " (substr *posted-by 5) 
                *rp* *nl* *nl*
                mbody
                "\r\n"
                ) 
         a (ss+ plain "r-Encoding: ")
         b (ss plain "\r\n" a)
         encoding (substr plain a b)
         html 
          (if (ss encoding "quo")  ; quoted-printable
              (html-quoted plain)
              (str *boundary "\r\n"
                   "Content-Type: text/html; charset=ISO-8859-1\r\n"
                   "Content-Transfer-Encoding: " encoding "\r\n\r\n"
                   *html-doctype
                   "<html><body>"
                   "<p>"
                   *lp*
                   "Posted by <i>" (substr *posted-by 0 atp)
                   "</i> at email server <i>" (substr *posted-by atp) 
                   *rp* 
                   *nl*
                   "</i></p>"
                   "<input type='hidden' name='newsid' value=" 
                       *newsid "/>"
                   *nl*
                   ; mbody
                   "</body></html>\r\n\r\n"
                   )
               )
         ]
   (def *multi
     (str 
          "Content-Type: multipart/alternative;\r\n"
          "    boundary=\"" *boundary "\"\r\n\r\n"
          "This is a multi-part message in MIME format.\r\n"
          plain
          html
          *boundary "=="
          )
      )
    )
  )

(defn set-multi-html []     ; cloning from set-multi-plain
;(brk 'ctol ctol)
  (let [
         a (ss+ *m$ "\r\n\r\n")
         mbody (substr *m$ a)  
         x (def *header (substr *m$ 0 a))
         atp (ss *posted-by at$)
         plain
           (str *boundary "\r\n"
                (get-remove-content-data) 
                "\r\n\r\n"
                "\r\n"
                *lp*
                "Newsid=" *newsid *nl*
                "Posted by " (substr *posted-by 0 atp)
                   " at email server " (substr *posted-by atp) 
                *rp* 
                *nl* *nl*
                mbody
                "\r\n"
                )
         a (ss+ plain "r-Encoding: ")
         b (ss plain "\r\n" a)
         encoding (substr plain a b)
         html 
          (if (ss encoding "quo")  ; quoted-printable
              (html-quoted plain)
              (str *boundary "\r\n"
                   "Content-Type: text/html; charset=ISO-8859-1\r\n"
                   "Content-Transfer-Encoding: " encoding "\r\n\r\n"
                   *html-doctype
                   "<html><body>"
                   "<p>"
                   *lp*
                   "Posted by <i>" (substr *posted-by 0 atp)
                   "</i> at email server <i>" (substr *posted-by atp) 
                   *rp* 
                   *nl*
                   "</i></p>"
                   "<input type='hidden' name='newsid' value=" 
                       *newsid "/>"
                   *nl*
                   mbody
                   "</body></html>\r\n\r\n"
                   )
               )
         ]
   (def *multi
     (str 
          "Content-Type: multipart/alternative;\r\n"
          "    boundary=\"" *boundary "\"\r\n\r\n"
          "This is a multi-part message in MIME format.\r\n"
          plain
          html
          *boundary "=="
          )
      )
    )
  )

(defn set-multi [ctol]
  (let [ pa (vassoc "plain" ctol)
         ha (vassoc "html" ctol)
         ]
    (cond (and ha pa)
            (set-multi-multi pa ha)
          pa 
            (set-multi-plain)
          ha
            (set-multi-html)
          )
    ) )
          

(comment
  (cond (sassoc "multi" ctol)
          (let [ a (ss *m$ "---" (vassoc "multi" ctol))
                 a (ss+ *m$ "boundary=\"" a)
                 b (ss *m$ \" (inc a))
                 b$ (substr *m$ a b)
                 ]
               (def *boundary b$)
               (def *multi (substr *m$ (ss *m$ b$ (inc b))))
               (def *header (substr *m$ 0 b))
(in-sink "testout.txt" '(println pars/*header *nl* pars/*multi))
(brk 'set-multi-multi)
               )
        (not (def *boundary (str "-------news@leftofdayton" (now))))
          nil
        (sassoc "plain" ctol)
          (set-multi-plain)
        (sassoc "html" ctol)
          (set-multi-html)
        )
  )
