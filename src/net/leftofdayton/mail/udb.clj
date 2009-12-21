(def* *udbi 3984 *udbm (hash-map))

(def *ueml
  '( "thorstein@leftofdayton.net"
     "don@leftofdayton.net" 
     "don.loritz@xizij.net"
     "dloritz2@xizij.net"
     "don.loritz@lexisnexis.com"
   ) )

(defn make-udbm []
  (def *udbm
      (loop [ l *ueml m  (hash-map) ]
         (if (empty? l)
             m
             (recur (rest l) 
                    (assoc m (first l) 
                             {:uid (binc *udbi 111)}
                             )
                    )
             ) )
       ) )
         
(and (empty? *udbm)
     (make-udbm)
     )
     
  
