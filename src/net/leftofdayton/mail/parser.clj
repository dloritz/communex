

(defn plain-to-html []
   (set! *html
         (str "<html><body>"
              *plain
              </body></html>
              ) )
   )

(defn pemit [ & l$]
  (set! *plain (str plain (apply str l$)))
  )

(defn html-gettag [b]
   (let [ a (ss *html$ \< b)
          sp (ss *html$ \ a)
          b (ss *html$ \> a)
          tag$ (and b (substr *html$ (inc a) (if sp (dec sp) (dec b))))
          ]
        (and tag$
             (trim-punc (substr tag$ 1))
             )
         ) )


(defn html2pl-tags []
     (iff (xright "<")
          (pemit (substr *x$ *j (dec *i)))
          (let [ tag$ (substr *x$ *i (xright \>))
                 x    (set! *j (inc *i))
                 tag  (substr tag$ 1 -1)
                 tag  (trim-punc
                          (substr tag (if (= \/ (nth tag 0)) 1 0 )))
                 ]
               (cond (memq tag '("/p" "/table"))
                        (pemit *nl* *nl*)
                     (memq tag '("table" "blockquote"))
                        (do (pemit *nl* *nl*)
                            (set! *nl* (str *nl* "   "))
                            )
                     (memq tag '("/table" "/blockquote"))
                        (do (set! *nl* "\r\n")
                            (pemit *nl* *nl*)
                            )
                     (memq tag '("/tr" "br"))
                        (pemit *nl*)
                     (memq tag '("/td" "/th"))
                        (pemit "  ")
                     )
               )
           (recur)
           )
     )

(defn html2pl-entities []
   (iff (xright \&)
        (let [ ent (de-entify (substr *plain (ss *x$ \; *i)))
               ]
           (set! *plain (str (substr *plain 0 (dec *i))
                             ent
                             (substr *plain (inc (ss *plain *i \;)))
                             ) )
           )
        (recur)
        )
   )


(defn html-to-*plain []
     (set! *j 0)
     (html2pl-tags)
     (set! *i 0)
     (html2pl-entities)
     )

(defn set-onepart []
   (let [ b (ss *m$ (str *nl* *nl*))
          c (ss+ *m$ "Content-Type: ")
          c (substr *m$ c (ss *m$ *nl* c))
          ]
        (def *header (substr *m$ 0 b))
        (cond (ss (lower c) "plain")
                 (def *plain (substr *m$ (+ b 4)))
              (ss (lower c) "html")
                 (def *html (substr *m$ (+ b 4)))
              )
        (if *plain
            (plain-to-html)
            (in-xrl-env (str \ *html)
                    (set! *plain "")
                    (html-to-plain)
                    )
            )
        ) )

(defn get-boundary [i]
   (ssl+ *m$ "\r\n" (ss *m$ *boundary i))
   )

(defn get-parts2 [partns new]
    (if (not (second partns))
        new
        (let [ bend (get-boundary (first partns))
               part (substr *m$ (first partns) bend)

               b (ss+ part "Content-Type:")
               ctype$
                 (and part
                      (substr part b (+ b 40)) )
                 ]
;(brk partns bend)
           (cond (not (second partns))
                   new
                 (ss ctype$ "text/plain")
                   (do (def *plain part)
                       (get-parts2 (rest partns) new)
                       )
                 (ss ctype$ "html")
                   (do (def *html part)
                       (get-parts2 (rest partns) new)
                       )
                 true
                   (get-parts2 (rest partns) (concat new (list part)))
                ) ) ) )

(defn get-parts [i]
   (loop [ a (ss+ *m$ *boundary i) ]
      (and a
          (def *parts (concat *parts (list a)))
          (recur (ss+ *m$ *boundary a))
          )
      )
   (def *header (substr *m$ 0 (ss *m$ "\r\n\r\n")))
   (def *part nil)
   (def *parts (get-parts2 *parts nil))
(brk '*parts)
   )

(defn set-multipart []
   (if (def *i (ss+ *m$ "boundary=\""))
       (def *boundary (substr *m$ *i (ss *m$ \" *i)) )
       (error "no boundary")
       )
   (def *i (ss *m$ *nl* *i))
   (def *parts nil)
   (def *parts (get-parts *i))
(brk 'smp)
   )


(defn parse [f$]
     (binding [*nl* *nl*]
        (def *m$ (slurp (str mail-folder "Inbox/" f$)))
        (set! plain$ "")
        (def *i 0)
        (if (ss *m$ "Content-Type: multipart")
            (set-multipart)
            (set-onepart)
            )
        )
     )

