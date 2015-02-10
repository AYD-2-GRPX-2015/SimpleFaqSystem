query.native.topranked:::
select    t.faq,
          t.ranking,
          t.sumOfRankings,
          t.numOfRankings
from 
        (	select 	faq, 
                                (sum(level)/count(*)) as ranking,
                                sum(level) as sumOfRankings, 
                                count(*) as numOfRankings 
                 from sf_faq_score 
                 group by faq limit 20 
         ) as t 
order by 
        ranking desc,numOfRankings desc
;;;


query.Faq.topvisited:::
SELECT f FROM Faq f 
WHERE f.estado = 1
ORDER BY f.visits DESC
;;;


query.newfaqs:::
SELECT f FROM Faq f 
WHERE f.estado = 1
ORDER BY f.postdate DESC
;;;


query.Faq.find:::
SELECT f 
FROM Faq f 
WHERE 
    f.estado = 1 and
    (f.title like :title or 
    f.shortDescription like :short_description or 
    f.longDescription like :long_description)
;;;


query.native.topicfaqcount::: 
SELECT  
        f.topic as topicId, 
        count(*) as faqCount 
FROM sf_faq f 
WHERE  
        f.estado = 1 GROUP BY f.topic

;;;

query.FaqTopic.findRoot::: SELECT f FROM FaqTopic f WHERE f.parent IS NULL
;;;