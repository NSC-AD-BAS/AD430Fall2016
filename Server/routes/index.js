
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'AD430' });
  document.write(req);
};
